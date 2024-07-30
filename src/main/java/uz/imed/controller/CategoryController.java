package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Category;
import uz.imed.payload.ApiResponse;
import uz.imed.service.CategoryService;

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
}
