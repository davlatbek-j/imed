package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Category;
import uz.imed.exception.JsonParsingException;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.CategoryDTO;
import uz.imed.repository.CatalogRepository;
import uz.imed.repository.CatalogTranslationsRepository;
import uz.imed.repository.CategoryRepository;
import uz.imed.repository.CategoryTranslationRepository;
import uz.imed.util.SlugUtil;

import java.util.Optional;

@RequiredArgsConstructor

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;
    private final CatalogRepository catalogRepository;
    private final ObjectMapper objectMapper;
    private final CategoryTranslationRepository categoryTranslationRepo;
    private final CatalogTranslationsRepository catalogTranslationRepo;
    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Category>> add(String json, MultipartFile photo)
    {
        ApiResponse<Category> response = new ApiResponse<>();
        try
        {
            Category category = objectMapper.readValue(json, Category.class);
            Category saved = categoryRepository.save(category);
            saved.setSlug(saved.getId() + "-" + SlugUtil.makeSlug(saved.getNameUz()));
            saved.setPhoto(photoService.save(photo));

            response.setData(categoryRepository.save(saved));
            response.setMessage("Category added");
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e)
        {
            throw new JsonParsingException("Error parsing JSON: " + e.getMessage());
        }
    }

    public ResponseEntity<ApiResponse<?>> get(String slug, String lang)
    {
        if (lang == null)
        {
            ApiResponse<Category> response = new ApiResponse<>();
            Optional<Category> bySlug = categoryRepository.findBySlugIgnoreCase(slug);
            Category category = bySlug.orElseThrow(() -> new NotFoundException("Category not found by slug: " + slug));
            response.setData(category);
            response.setMessage("Found all language(s)");
            return ResponseEntity.ok(response);
        } else
        {
            ApiResponse<CategoryDTO> response = new ApiResponse<>();
            Optional<Category> bySlug = categoryRepository.findBySlugIgnoreCase(slug);
            Category category = bySlug.orElseThrow(() -> new NotFoundException("Category not found by slug: " + slug));
            response.setData(new CategoryDTO(category, lang));
            response.setMessage("Found in language: " + lang);
            return ResponseEntity.ok(response);
        }
    }
}
