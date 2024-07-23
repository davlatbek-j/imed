package uz.imed.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.imed.entity.Catalog;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.CatalogDTO;
import uz.imed.payload.ProductDTO;
import uz.imed.service.CatalogService;
import uz.imed.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    private final ProductService productService;

    @PostMapping("/create/{categoryId}")
    public ResponseEntity<ApiResponse<CatalogDTO>> create(
            @PathVariable Long categoryId,
            @RequestBody Catalog catalog
    ) {
        return catalogService.create(categoryId, catalog);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<CatalogDTO>> findById(
            @PathVariable String slug
    ) {
        return catalogService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<CatalogDTO>>> findAll() {
        return catalogService.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CatalogDTO>> update(
            @PathVariable Long id,
            @RequestBody Catalog catalog
    ) {
        return catalogService.update(id, catalog);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return catalogService.delete(id);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return catalogService.changeActive(id);
    }

    @GetMapping("/products/{slug}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProductBySlug(
            @PathVariable String slug
    ) {
        return productService.findAllByCatalogSlug(slug);
    }

}
