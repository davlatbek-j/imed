package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Category;
import uz.imed.entity.CategoryItem;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.CategoryItemDTO;
import uz.imed.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private  final CategoryService categoryService;

    @PostMapping("/add-item")
    public ResponseEntity<ApiResponse<Category>> addItem(
            @RequestBody CategoryItem categoryItem,
            @RequestParam(name = "photo")MultipartFile file
            ){
        return categoryService.addItem(categoryItem,file);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Category>> get(
            @RequestParam(value = "main", required = false) Boolean main,
            @RequestParam(value = "active", required = false) Boolean active)
    {
        return categoryService.get(main, active);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<CategoryItem>> getItem(
            @PathVariable String slug)
    {
        return categoryService.getItem(slug);
    }

    @GetMapping("name-list")
    public ResponseEntity<ApiResponse<List<CategoryItemDTO>>> getNameList(
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang
    )
    {
        return categoryService.getNameList(lang);
    }


    @PutMapping
    public ResponseEntity<ApiResponse<Category>> update(
            @RequestBody Category category)
    {
        return categoryService.update(category);
    }

    @DeleteMapping("/delete/{item-id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable("item-id") Long itemId)
    {
        return categoryService.delete(itemId);
    }


}
