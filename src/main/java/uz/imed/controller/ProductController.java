package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Product;
import uz.imed.entity.Review;
import uz.imed.payload.ApiResponse;
import uz.imed.service.FileService;
import uz.imed.service.ProductService;
import uz.imed.service.ReviewService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor

@Controller
@RequestMapping("/v1/product")
public class ProductController
{
    private final ProductService productService;
    private final ReviewService reviewService;
    private final FileService fileService;

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
            @RequestParam(value = "product-id") Long productId,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "2", required = false) int size)
    {
        return reviewService.get(lang, productId, page, size);
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

    //------------File upload--------
    @PostMapping("/file")
    public ResponseEntity<ApiResponse<?>> uploadFile(
            @RequestParam(value = "files") List<MultipartFile> files,
            @RequestParam(value = "product-id") Long productId)
    {
        return fileService.upload(productId, files);
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileName)
    {
        return fileService.download(fileName);
    }

}
