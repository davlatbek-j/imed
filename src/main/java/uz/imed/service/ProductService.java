package uz.imed.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Catalog;
import uz.imed.entity.Product;
import uz.imed.entity.ProductCharacteristic;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ProductDTO;
import uz.imed.payload.ProductForListDTO;
import uz.imed.repository.CatalogRepository;
import uz.imed.repository.ProductRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CatalogRepository catalogRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Product>> create(Long catalogId, String json, MultipartFile mainPhotoFile, List<MultipartFile> photoFiles) {
        ApiResponse<Product> response = new ApiResponse<>();
        Optional<Catalog> optionalCatalog = catalogRepository.findById(catalogId);
        if (optionalCatalog.isEmpty()) {
            response.setMessage("Catalog is not found by id: " + catalogId);
            return ResponseEntity.status(404).body(response);
        }
        try {
            Product product = objectMapper.readValue(json, Product.class);
            product.getCharacteristic().forEach(characteristic -> characteristic.setProduct(product));
            product.setMainPhotoUrl(photoService.save(mainPhotoFile).getHttpUrl());
            product.setActive(true);
            product.setSalePrice(product.getPrice()-(product.getPrice())*product.getSale()*0.01);
            product.setCatalog(optionalCatalog.get());
            product.getCharacteristic().forEach(characteristic -> characteristic.setProduct(product));
            product.setPhotoUrls(new ArrayList<>());
            for (MultipartFile photo : photoFiles) {
                product.getPhotoUrls().add(photoService.save(photo).getHttpUrl());
            }
            Product save = productRepository.save(product);
            String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getName());
            productRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(401).body(response);
        }
    }

    public ResponseEntity<ApiResponse<Product>> findById(Long id) {
        ApiResponse<Product> response = new ApiResponse<>();
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            response.setMessage("Product is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Product product = optionalProduct.get();
        response.setMessage("Found");
        response.setData(product);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Product>> findBySlug(String slug) {
        ApiResponse<Product> response = new ApiResponse<>();
        Optional<Product> optionalProduct = productRepository.findBySlug(slug);
        if (optionalProduct.isEmpty()) {
            response.setMessage("Product is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        Product product = optionalProduct.get();
        response.setData(product);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<ProductForListDTO>>> findAll() {
        ApiResponse<List<ProductForListDTO>> response = new ApiResponse<>();
        List<Product> all = productRepository.findAll();
        response.setData(new ArrayList<>());
        all.forEach(product -> response.getData().add(new ProductForListDTO(product)));
        response.setMessage("Found " + all.size() + " product(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Page<ProductForListDTO>>> findAllByPageNation(int page, int size) {
        ApiResponse<Page<ProductForListDTO>> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> all = productRepository.findAll(pageable);
        Page<ProductForListDTO> map = all.map(ProductForListDTO::new);
        response.setData(map);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<ProductForListDTO>>> similarProduct(String productSlug) {
        ApiResponse<List<ProductForListDTO>> response = new ApiResponse<>();
        String catalogSlug = productRepository.findCatalogSlugByProductSlug(productSlug);
        if (catalogSlug == null) {
            response.setMessage("Product is not found by slug: " + productSlug);
            return ResponseEntity.status(404).body(response);
        }
        response.setData(new ArrayList<>());
        List<Product> all = productRepository.findAll();
        all.stream()
                .filter(product -> (product.getCatalog().getSlug().equals(catalogSlug) && !product.getSlug().equals(productSlug)))
                .limit(4)
                .toList()
                .forEach(product -> response.getData().add(new ProductForListDTO(product)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<ProductDTO>>> findAllByCatalogSlug(String catalogSlug) {
        ApiResponse<List<ProductDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Product> all = productRepository.findAll();
        all.stream()
                .filter(product -> product.getCatalog().getSlug().equals(catalogSlug))
                .toList()
                .forEach(product -> response.getData().add(new ProductDTO(product)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<ProductDTO>>> findAllByCategorySlug(String categorySlug) {
        ApiResponse<List<ProductDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Product> all = productRepository.findAll();
        all.stream()
                .filter(product -> product.getCatalog().getCategory().getSlug().equals(categorySlug))
                .toList()
                .forEach(product -> response.getData().add(new ProductDTO(product)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Product>> update(Long id, String newJson, MultipartFile newPhoto, List<MultipartFile> newPhotoList) {
        ApiResponse<Product> response = new ApiResponse<>();
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            response.setMessage("Product is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Product oldProduct = optionalProduct.get();
        String oldMainPhotoUrl = oldProduct.getMainPhotoUrl();
        List<String> oldPhotoUrls = oldProduct.getPhotoUrls();
        boolean active = oldProduct.isActive();
        String slug = oldProduct.getSlug();

        Product newProduct;
        try {
            if (newJson != null) {
                newProduct = objectMapper.readValue(newJson, Product.class);
                newProduct.setId(id);
                newProduct.setActive(active);
                newProduct.setSlug(slug);
                newProduct.setSalePrice(newProduct.getPrice() - (newProduct.getPrice() * newProduct.getSale() * 0.01));
                newProduct.setCatalog(oldProduct.getCatalog());

                // Eski characteristiclarni olib tashlash
                oldProduct.getCharacteristic().clear();
                productRepository.save(oldProduct);

                // Yangi characteristiclarni bog'lash
                for (ProductCharacteristic characteristic : newProduct.getCharacteristic()) {
                    characteristic.setProduct(newProduct);
                }
            } else {
                newProduct = oldProduct;
            }

            if (newPhoto == null || newPhoto.isEmpty()) {
                newProduct.setMainPhotoUrl(oldMainPhotoUrl);
            } else {
                newProduct.setMainPhotoUrl(photoService.save(newPhoto).getHttpUrl());
            }

            if (newPhotoList == null || newPhotoList.isEmpty()) {
                newProduct.setPhotoUrls(oldPhotoUrls);
            } else {
                newProduct.setPhotoUrls(new ArrayList<>());
                for (MultipartFile photo : newPhotoList) {
                    newProduct.getPhotoUrls().add(photoService.save(photo).getHttpUrl());
                }
            }

            Product save = productRepository.save(newProduct);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }


    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            response.setMessage("Product is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Product product = optionalProduct.get();
        boolean active = !product.isActive();
        productRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current product active: " + active);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (productRepository.findById(id).isEmpty()) {
            response.setMessage("Product is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        productRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

}