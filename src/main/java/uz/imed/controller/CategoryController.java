package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Category;
import uz.imed.payload.ApiResponse;
import uz.imed.service.CategoryService;

import java.util.List;

@RequiredArgsConstructor

@Controller
@RequestMapping("/api/category")
public class CategoryController
{
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> addCategory(
            @RequestParam("json") String json,
            @RequestParam("photo") MultipartFile photo)
    {
        return categoryService.add(json, photo);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<?>> getCategorySlug(
            @RequestHeader(value = "Accept-Language", required = false) String lang,
            @PathVariable String slug)
    {
        return categoryService.get(slug, lang);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<?>>> getAll(
            @RequestHeader(value = "Accept-Language", required = false) String lang)
    {
        return categoryService.getAll()
    }
}
