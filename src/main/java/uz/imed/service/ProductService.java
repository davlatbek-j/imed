package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.*;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ProductDTO;
import uz.imed.payload.ProductMainDataDTO;
import uz.imed.repository.*;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor

@Service
public class ProductService
{
    private final ProductRepository productRepo;
    private final ObjectMapper objectMapper;
    private final PhotoService photoService;
    private final PartnerRepository partnerRepository;
    private final CatalogRepository catalogRepository;
    private final CategoryRepository categoryRepo;
    private final DescriptionRepository descriptionRepo;
    private final ReviewOptionRepository reviewOptionRepo;
    private final ReviewRepository reviewRepo;
    private final CharacteristicRepository characteristicRepo;

    public ResponseEntity<ApiResponse<Product>> add(String json, List<MultipartFile> gallery, List<MultipartFile> reviewDoctorsPhoto)
    {
        ApiResponse<Product> response = new ApiResponse<>();
        try
        {
            Product product = objectMapper.readValue(json, Product.class);

            if (product.getPartner() == null)
                throw new NotFoundException("Partner not given");
            product.setPartner(partnerRepository.findById(product.getPartner().getId()).orElseThrow(() -> new NotFoundException("Partner not found by id: " + product.getPartner().getId())));

            if (product.getCatalog() != null && product.getCategory() != null)
                throw new NotFoundException("Product belongs to only catalog or category. Not both");

            if (product.getCatalog() != null)
            {
                Catalog catalog = catalogRepository.findById(product.getCatalog().getId()).orElseThrow(() -> new NotFoundException("Catalog not found by id: " + product.getCatalog().getId()));
                product.setCatalog(catalog);
                product.setCategory(catalog.getCategory());
            }

            if (product.getCategory() != null)
                product.setCategory(categoryRepo.findById(product.getCategory().getId()).orElseThrow(() -> new NotFoundException("Category not found id: " + product.getCategory().getId())));

            Long id = productRepo.save(new Product()).getId();
            product.setId(id);
            product.setSlug(id + "-" + SlugUtil.makeSlug(product.getName()));

            if (product.getDescriptions() != null)
                product.getDescriptions().forEach(i -> i.setProduct(product));
            if (product.getCharacteristics() != null)
                product.getCharacteristics().forEach(i -> i.setProduct(product));
            if (product.getReviews() != null)
            {
                product.getReviews().forEach(i ->
                {
                    i.getOptions().forEach(j -> j.setReview(i));
                    i.setProduct(product);
                });
                if (reviewDoctorsPhoto == null || product.getReviews().size() != reviewDoctorsPhoto.size())
                    throw new NotFoundException("Number of review(s): " + product.getReviews().size() +
                            "\nNumber of 'review-doctors-photo': " + (reviewDoctorsPhoto != null ? reviewDoctorsPhoto.size() : 0) + "\nBoth size(s) must match!!!");

                for (int i = 0; i < reviewDoctorsPhoto.size(); i++)
                    product.getReviews().get(i).setDoctorPhoto(photoService.save(reviewDoctorsPhoto.get(i)));

            }

            product.setGallery(new ArrayList<>());
            gallery.forEach(i -> product.getGallery().add(photoService.save(i)));

            response.setData(productRepo.save(product));
            response.setMessage("Product added");

            return ResponseEntity.status(201).body(response);

        } catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error parsing json" + e.getMessage());
        }
    }

    public ResponseEntity<ApiResponse<?>> get(String slug, String lang, boolean similar)
    {
        Product product = productRepo.findBySlug(slug).orElseThrow(() -> new NotFoundException("Product not not found by slug:" + slug));

        if (similar && lang != null)
        {
            ApiResponse<List<ProductMainDataDTO>> response = new ApiResponse<>();
            Category category = product.getCategory();
            List<Product> list = productRepo.findAllByCategoryId(category.getId());
            list.removeIf(i -> i.getId().equals(product.getId()));

            response.setData(new ArrayList<>());
            list.forEach(i -> response.getData().add(new ProductMainDataDTO(i, lang)));
            response.setMessage("Similar products");
            return ResponseEntity.status(200).body(response);
        }

        if (lang == null)
        {
            ApiResponse<Product> response = new ApiResponse<>();
            response.setData(product);
            response.setMessage("All language");
            return ResponseEntity.status(200).body(response);
        } else
        {
            ApiResponse<ProductDTO> response = new ApiResponse<>();
            response.setData(new ProductDTO(product, lang));
            response.setMessage("Found: " + lang);
            return ResponseEntity.status(200).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> all(String lang, Long categoryId, Long catalogId, Boolean popular)
    {
        if (catalogId != null && categoryId != null)
            throw new NotFoundException("You can filter by only either Catalog id or Category id. Not both");

        if (popular != null)
        {
            ApiResponse<List<ProductMainDataDTO>> response = new ApiResponse<>();

            List<Product> all = productRepo.findAllByPopular(popular);
            all.forEach(i -> response.getData().add(new ProductMainDataDTO(i, lang)));
            response.setMessage("Popular " + all.size() + " products");
            return ResponseEntity.status(200).body(response);
        }

        ApiResponse<List<ProductMainDataDTO>> response = new ApiResponse<>();
        List<Product> all;
        response.setData(new ArrayList<>());

        if (categoryId == null && catalogId == null)
        {
            all = productRepo.findAll();
            all.forEach(i -> response.getData().add(new ProductMainDataDTO(i, lang)));
            response.setMessage("Found " + all.size() + " products");
            return ResponseEntity.status(200).body(response);
        }
        if (categoryId != null)
        {
            all = productRepo.findAllByCategoryId(categoryId);
            all.forEach(i -> response.getData().add(new ProductMainDataDTO(i, lang)));
            response.setMessage("Category(id=" + categoryId + ") " + all.size() + " products");

        } else
        {
            all = productRepo.findAllByCatalogId(catalogId);
            all.forEach(i -> response.getData().add(new ProductMainDataDTO(i, lang)));
            response.setMessage("Catalog(id=" + catalogId + ") " + all.size() + " products");
        }
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id)
    {
        if (!productRepo.existsById(id))
            throw new NotFoundException("Product not found by id: " + id);

        ApiResponse<?> response = new ApiResponse<>();
        productRepo.deleteById(id);
        response.setMessage("Product deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Product>> update(Product newProduct)
    {
        if (newProduct == null)
            throw new NotFoundException("Product not given");

        Product fromDB = productRepo.findById(newProduct.getId()).orElseThrow(() -> new NotFoundException("Product not found id: " + newProduct.getId()));
        Long productId = fromDB.getId();

        //--------------Name--------------
        if (newProduct.getName() != null)
        {
            fromDB.setName(newProduct.getName());
            fromDB.setSlug(productId + "-" + SlugUtil.makeSlug(newProduct.getName()));
        }

        //--------------Tag--------------
/*        if (newProduct.getTagUz() != null)
            fromDB.setTagUz(newProduct.getTagUz());

        if (newProduct.getTagRu() != null)
            fromDB.setTagRu(newProduct.getTagRu());

        if (newProduct.getTagEn() != null)
            fromDB.setTagEn(newProduct.getTagEn());*/

        if (newProduct.getANew() != null)
            fromDB.setANew(newProduct.getANew());

        if (newProduct.getSale() != null)
            fromDB.setSale(newProduct.getSale());

        //--------------ShortDescription--------------
        if (newProduct.getShortDescriptionUz() != null)
            fromDB.setShortDescriptionUz(newProduct.getShortDescriptionUz());

        if (newProduct.getShortDescriptionRu() != null)
            fromDB.setShortDescriptionRu(newProduct.getShortDescriptionRu());

        if (newProduct.getShortDescriptionEn() != null)
            fromDB.setShortDescriptionEn(newProduct.getShortDescriptionEn());

        //--------------Discount--------------
        if (newProduct.getDiscount() != null)
        {
            if (newProduct.getDiscount() < 100 && newProduct.getDiscount() >= 0)
                fromDB.setDiscount(newProduct.getDiscount());
            else
                throw new NotFoundException("Invalid discount value: " + newProduct.getDiscount());
        }

        //--------------Original Price--------------
        if (newProduct.getOriginalPrice() != null)
        {
            if (newProduct.getOriginalPrice() >= 0)
                fromDB.setOriginalPrice(newProduct.getOriginalPrice());
            else
                throw new NotFoundException("Invalid original price value: " + newProduct.getOriginalPrice());
        }

        //--------------Conditions--------------
        if (newProduct.getConditionsUz() != null)
            fromDB.setConditionsUz(newProduct.getConditionsUz());

        if (newProduct.getConditionsRu() != null)
            fromDB.setConditionsRu(newProduct.getConditionsRu());

        if (newProduct.getConditionsEn() != null)
            fromDB.setConditionsEn(newProduct.getConditionsEn());

        //--------------Active--------------
        if (newProduct.getActive() != null)
            fromDB.setActive(newProduct.getActive());

        //--------------Popular--------------
        if (newProduct.getActive() != null)
            fromDB.setActive(newProduct.getActive());


        //--------------Description--------------
        if (newProduct.getDescriptions() != null && !newProduct.getDescriptions().isEmpty())
        {
            List<Description> newDescriptions = newProduct.getDescriptions();
            List<Description> dbDescriptions = fromDB.getDescriptions();
            if (dbDescriptions == null)
                dbDescriptions = new ArrayList<>();

            for (Description newDescription : newDescriptions)
            {
                if (newDescription.getId() != null)
                {
                    for (Description dbDescription : dbDescriptions)
                    {
                        if (newDescription.getId().equals(dbDescription.getId()))
                        {
                            boolean leastOneGiven = false;
                            if (newDescription.getTitleUz() != null)
                            {
                                dbDescription.setTitleUz(newDescription.getTitleUz());
                                leastOneGiven = true;
                            }

                            if (newDescription.getTitleRu() != null)
                            {
                                dbDescription.setTitleRu(newDescription.getTitleRu());
                                leastOneGiven = true;
                            }

                            if (newDescription.getTitleEn() != null)
                            {
                                dbDescription.setTitleEn(newDescription.getTitleEn());
                                leastOneGiven = true;
                            }

                            if (newDescription.getValueUz() != null)
                            {
                                dbDescription.setValueUz(newDescription.getValueUz());
                                leastOneGiven = true;
                            }

                            if (newDescription.getValueRu() != null)
                            {
                                dbDescription.setValueRu(newDescription.getValueRu());
                                leastOneGiven = true;
                            }

                            if (newDescription.getValueEn() != null)
                            {
                                dbDescription.setValueEn(newDescription.getValueEn());
                                leastOneGiven = true;
                            }

                            //if id of description given but no other parameters not given accordingly delete it
                            if (!leastOneGiven)
                            {
                                descriptionRepo.delete(newDescription.getId());
                            }
                        }

                    }
                }
//                if id not given save it
                else
                {
                    newDescription.setProduct(fromDB);
                    dbDescriptions.add(newDescription);
//                    descriptionRepo.save(newDescription);
                }
            }

        }

        //--------------Partner--------------
        if (newProduct.getPartner() != null && newProduct.getPartner().getId() != null)
        {
            Long partnerId = newProduct.getPartner().getId();
            Partner partner = partnerRepository.findById(partnerId).orElseThrow(() -> new NotFoundException("Partner not found by id: " + partnerId));
            fromDB.setPartner(partner);
        }


        //--------------Catalog--------------
        boolean catalogGiven = false;
        if (newProduct.getCatalog() != null && newProduct.getCatalog().getId() != null)
        {
            Long catalogId = newProduct.getCatalog().getId();
            Catalog catalog = catalogRepository.findById(catalogId).orElseThrow(() -> new NotFoundException("Catalog not found by id: " + catalogId));
            fromDB.setCategory(catalog.getCategory());
            fromDB.setCatalog(catalog);
            catalogGiven = true;
        }

        //--------------Category--------------
        if (newProduct.getCategory() != null && newProduct.getCategory().getId() != null)
        {
            if (catalogGiven)
                throw new NotFoundException("You can only set either Catalog or Category. Not both");

            Long categoryId = newProduct.getCategory().getId();
            Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found by id: " + categoryId));
            fromDB.setCategory(category);
        }

        //--------------Characteristics--------------
        if (newProduct.getCharacteristics() != null && !newProduct.getCharacteristics().isEmpty())
        {
            List<Characteristic> newCharacteristics = newProduct.getCharacteristics();
            List<Characteristic> dbCharacteristics = fromDB.getCharacteristics();
            if (dbCharacteristics == null)
                dbCharacteristics = new ArrayList<>();

            for (Characteristic newCharacteristic : newCharacteristics)
            {
                if (newCharacteristic.getId() != null)
                {
                    for (Characteristic dbCharacteristic : dbCharacteristics)
                    {
                        if (newCharacteristic.getId().equals(dbCharacteristic.getId()))
                        {
                            boolean leastOneGiven = false;
                            if (newCharacteristic.getTitleUz() != null)
                            {
                                dbCharacteristic.setTitleUz(newCharacteristic.getTitleUz());
                                leastOneGiven = true;
                            }

                            if (newCharacteristic.getTitleRu() != null)
                            {
                                dbCharacteristic.setTitleRu(newCharacteristic.getTitleRu());
                                leastOneGiven = true;
                            }

                            if (newCharacteristic.getTitleEn() != null)
                            {
                                dbCharacteristic.setTitleEn(newCharacteristic.getTitleEn());
                                leastOneGiven = true;
                            }

                            if (newCharacteristic.getValueUz() != null)
                            {
                                dbCharacteristic.setValueUz(newCharacteristic.getValueUz());
                                leastOneGiven = true;
                            }

                            if (newCharacteristic.getValueRu() != null)
                            {
                                dbCharacteristic.setValueRu(newCharacteristic.getValueRu());
                                leastOneGiven = true;
                            }

                            if (newCharacteristic.getValueEn() != null)
                            {
                                dbCharacteristic.setValueEn(newCharacteristic.getValueEn());
                                leastOneGiven = true;
                            }

                            //if not given least one parameter not given accordingly delete it
                            if (!leastOneGiven)
                            {
                                System.err.println("DELETE " + newCharacteristic.getId());
                                characteristicRepo.deleteId(newCharacteristic.getId());
                            }
                        }

                    }
                }
//                if id not given save it
                else
                {
                    newCharacteristic.setProduct(fromDB);
                    dbCharacteristics.add(newCharacteristic);
//                    descriptionRepo.save(newCharacteristics);
                }
            }

        }

        //--------------Review--------------
        if (newProduct.getReviews() != null && !newProduct.getReviews().isEmpty())
        {
            List<Review> newReviews = newProduct.getReviews();
            List<Review> dbReviews = fromDB.getReviews();
            if (dbReviews == null)
                dbReviews = new ArrayList<>();

            for (Review newReview : newReviews)
            {
                if (newReview.getId() != null)
                {
                    for (Review dbReview : dbReviews)
                    {
                        if (newReview.getId().equals(dbReview.getId()))
                        {
                            boolean leastOneGiven = false;

                            //--------------Name of doctor--------------
                            if (newReview.getNameDoctorUz() != null)
                            {
                                dbReview.setNameDoctorUz(newReview.getNameDoctorUz());
                                leastOneGiven = true;
                            }

                            if (newReview.getNameDoctorRu() != null)
                            {
                                dbReview.setNameDoctorRu(newReview.getNameDoctorRu());
                                leastOneGiven = true;
                            }

                            if (newReview.getNameDoctorEn() != null)
                            {
                                dbReview.setNameDoctorEn(newReview.getNameDoctorEn());
                                leastOneGiven = true;
                            }

                            //--------------Position doctor--------------
                            if (newReview.getPositionUz() != null)
                            {
                                dbReview.setPositionUz(newReview.getPositionUz());
                                leastOneGiven = true;
                            }

                            if (newReview.getPositionRu() != null)
                            {
                                dbReview.setPositionRu(newReview.getPositionRu());
                                leastOneGiven = true;
                            }
                            if (newReview.getPositionEn() != null)
                            {
                                dbReview.setPositionEn(newReview.getPositionEn());
                                leastOneGiven = true;
                            }

                            //--------------Review Options--------------
                            if (newReview.getOptions() != null && !newReview.getOptions().isEmpty())
                            {
                                updateReviewOptions(dbReview, newReview.getOptions());
                                leastOneGiven = true;
                            }

                            //if not given the least one parameter delete it
                            if (!leastOneGiven)
                            {
                                reviewRepo.deleteById(newReview.getId());
                            }

                        }

                    }

                }
                //if id not given save it
                else
                {
                    newReview.setProduct(fromDB);
                    dbReviews.add(newReview);
                    newReview.getOptions().forEach(i ->
                            {
                                if (i.getId() != null)
                                    throw new NotFoundException("You can't send option with id WHERE REVIEW ID NOT GIVEN");
                                i.setReview(newReview);
                            }
                    );
                    reviewRepo.save(newReview);
                }
            }

        }

        Product update = productRepo.save(fromDB);

        ApiResponse<Product> response = new ApiResponse<>();
        response.setMessage("Updated");
        response.setData(update);
        return ResponseEntity.ok(response);
    }

    private void updateReviewOptions(Review dbReview, List<ReviewOption> newOptions)
    {
        List<ReviewOption> dbOptions = dbReview.getOptions();
        if (dbOptions == null)
            dbOptions = new ArrayList<>();

        for (ReviewOption newOption : newOptions)
        {
            if (newOption.getId() != null)
            {

                for (ReviewOption dbOption : dbOptions)
                {
                    if (dbOption.getId().equals(newOption.getId()))
                    {
                        boolean leastOneGiven = false;

                        //--------------Title--------------
                        if (newOption.getTitleUz() != null)
                        {
                            dbOption.setTitleUz(newOption.getTitleUz());
                            leastOneGiven = true;
                        }

                        if (newOption.getTitleRu() != null)
                        {
                            dbOption.setTitleRu(newOption.getTitleRu());
                            leastOneGiven = true;
                        }

                        if (newOption.getTitleEn() != null)
                        {
                            dbOption.setTitleEn(newOption.getTitleEn());
                            leastOneGiven = true;
                        }

                        //--------------Value--------------
                        if (newOption.getValueUz() != null)
                        {
                            dbOption.setValueUz(newOption.getValueUz());
                            leastOneGiven = true;
                        }

                        if (newOption.getValueRu() != null)
                        {
                            dbOption.setValueRu(newOption.getValueRu());
                            leastOneGiven = true;
                        }

                        if (newOption.getValueEn() != null)
                        {
                            dbOption.setValueEn(newOption.getValueEn());
                            leastOneGiven = true;
                        }
                        //if not given the least one delete it
                        if (!leastOneGiven)
                        {
                            System.err.println("DELETE OPTION " + newOption.getId());
                            reviewOptionRepo.deleteByOptionId(newOption.getId());
                        }

                    }

                }

            }
            //if id not given save it
            else
            {
                newOption.setReview(dbReview);
                dbReview.getOptions().add(newOption);
//                reviewOptionRepo.save(newOption);
            }
        }
    }
}
