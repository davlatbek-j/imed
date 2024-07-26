/*
package uz.imed.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Product;
import uz.imed.payload.ApiResponse;

import uz.imed.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create/{catalogId}")
    public ResponseEntity<ApiResponse<Product>> create(
            @PathVariable Long catalogId,
            @RequestParam(value = "json") String product,
            @RequestPart(value = "mainPhoto") MultipartFile mainPhoto,
            @RequestPart(value = "photos") List<MultipartFile> photos
    ) {
        return productService.create(catalogId, product, mainPhoto, photos);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<Page<ProductForListDTO>>> findAllByPageNation(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "15") Integer size
    ) {
        return productService.findAllByPageNation(page, size);
    }

    @GetMapping("/get-similar/{productSlug}")
    public ResponseEntity<ApiResponse<List<ProductForListDTO>>> findSimilarProductList(
            @PathVariable String productSlug
    ) {
        return productService.similarProduct(productSlug);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<Product>> findBySlug(
            @PathVariable String slug
    ) {
        return productService.findBySlug(slug);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Product>> update(
            @PathVariable Long id,
            @RequestParam(value = "json") String product,
            @RequestPart(value = "mainPhoto") MultipartFile mainPhoto,
            @RequestPart(value = "photos") List<MultipartFile> photos
    ) {
        return productService.update(id, product, mainPhoto, photos);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return productService.changeActive(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return productService.delete(id);
    }

}*/
