package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Product;
import uz.imed.entity.Review;
import uz.imed.payload.ApiResponse;
import uz.imed.service.ProductService;
import uz.imed.service.ReviewService;

import java.util.List;

@RequiredArgsConstructor

@Controller
@RequestMapping("/v1/product")
public class ProductController
{
    private final ProductService productService;
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> addProduct(
            @RequestParam(value = "json") String json,
            @RequestParam(value = "gallery") List<MultipartFile> gallery,
            @RequestParam(value = "review-doctors-photo", required = false) List<MultipartFile> reviewDoctorsPhoto)
    {
        return productService.add(json, gallery, reviewDoctorsPhoto);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<?>> get(
            @RequestHeader(value = "Accept-Language", required = false) String lang,
            @PathVariable("slug") String slug,
            @RequestParam(value = "similar", required = false, defaultValue = "false") boolean similar)
    {
        return productService.get(slug, lang, similar);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> all(
            @RequestHeader(value = "Accept-Language") String lang,
            @RequestParam(value = "category-id", required = false) Long categoryId,
            @RequestParam(value = "catalog-id", required = false) Long catalogId,
//            @RequestParam(value = "tag", required = false) String tag,
//            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "popular", required = false) Boolean popular
    )
    {
        return productService.all(lang, categoryId, catalogId, popular);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Product>> update(
            @RequestBody Product product)
    {
        return productService.update(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id)
    {
        return productService.delete(id);
    }

    //-----------------Review----------------

    @PostMapping("/review")
    public ResponseEntity<ApiResponse<Review>> addReview(
            @RequestParam(value = "json") String json,
            @RequestParam(value = "product-id") Long productId,
            @RequestParam(value = "doctor-photo") MultipartFile docPhoto)
    {
        return reviewService.add(json, productId, docPhoto);
    }

    @GetMapping("/review")
    public ResponseEntity<ApiResponse<?>> allReview(
            @RequestHeader(value = "Accept-Language", required = false) String lang,
            @RequestParam(value = "product-id") Long productId)
    {
        return reviewService.get(lang, productId);
    }

    @PutMapping("/review")
    public ResponseEntity<ApiResponse<Review>> update(
            @RequestBody Review review)
    {
        return reviewService.update(review);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<ApiResponse<?>> deleteReview(@PathVariable Long id)
    {
        return reviewService.delete(id);
    }
}
