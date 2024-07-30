package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Category;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.CatalogRepository;
import uz.imed.repository.CategoryRepository;

@RequiredArgsConstructor

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;
    private final CatalogRepository catalogRepository;
    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<Category>> add(String json, MultipartFile photo)
    {
        ApiResponse<Category> response = new ApiResponse<>();
        try
        {
            Category category = objectMapper.readValue(json, Category.class);
            System.err.println("category = " + category);

            response.setData(categoryRepository.save(category));
            response.setMessage("Saved");

            return ResponseEntity.ok(response);


        } catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error parsing json: " + e.getMessage());
        }
    }
}
