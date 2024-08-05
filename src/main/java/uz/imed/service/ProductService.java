package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Catalog;
import uz.imed.entity.Product;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ProductDTO;
import uz.imed.repository.CatalogRepository;
import uz.imed.repository.CategoryRepository;
import uz.imed.repository.PartnerRepository;
import uz.imed.repository.ProductRepository;
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
    private final CategoryRepository categoryRepository;

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
                product.setCategory(categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new NotFoundException("Category not found id: " + product.getCategory().getId())));

            Long id = productRepo.save(new Product()).getId();
            product.setId(id);
            product.setSlug(id + "-" + SlugUtil.makeSlug(product.getName()));

            if (product.getDescriptions() != null)
                product.getDescriptions().forEach(i -> i.setProduct(product));
            if (product.getCharacteristics() != null)
                product.getCharacteristics().forEach(i -> i.setProduct(product));
            if (product.getReviews() != null)
                product.getReviews().forEach(i ->
                {
                    i.getOptions().forEach(j -> j.setReview(i));
                    i.setProduct(product);
                });

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

    public ResponseEntity<ApiResponse<?>> get(String slug, String lang)
    {
        Product product = productRepo.findBySlug(slug).orElseThrow(() -> new NotFoundException("Slug not found"));
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

    public ResponseEntity<ApiResponse<List<ProductDTO>>> all(String lang, Long categoryId, Long catalogId, Boolean popular)
    {
        if (catalogId != null && categoryId != null)
            throw new NotFoundException("You can filter by only either Catalog id or Category id. Not both");

        ApiResponse<List<ProductDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());

        if (popular != null)
        {
            List<Product> all = productRepo.findAllByPopular(popular);
            all.forEach(i -> response.getData().add(new ProductDTO(i, lang)));
            response.setMessage("Found " + all.size() + " products");
            return ResponseEntity.status(200).body(response);
        }

        if (categoryId == null && catalogId == null)
        {
            List<Product> all = productRepo.findAll();
            all.forEach(i -> response.getData().add(new ProductDTO(i, lang)));
            response.setMessage("Found " + all.size() + " products");
            return ResponseEntity.status(200).body(response);
        }
        if (categoryId != null)
        {
            List<Product> all = productRepo.findAllByCategoryId(categoryId);
            all.forEach(i -> response.getData().add(new ProductDTO(i, lang)));
            response.setMessage("Found " + all.size() + " products");
            return ResponseEntity.status(200).body(response);

        } else
        {
            List<Product> all = productRepo.findAllByCatalogId(catalogId);
            all.forEach(i -> response.getData().add(new ProductDTO(i, lang)));
            response.setMessage("Found " + all.size() + " products");
            return ResponseEntity.status(200).body(response);
        }
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
}
