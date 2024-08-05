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
import uz.imed.payload.catalog.CategoryDTO;
import uz.imed.payload.catalog.CategoryNameDTO;
import uz.imed.repository.CatalogRepository;
import uz.imed.repository.CategoryRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;
    private final CatalogRepository catalogRepository;
    private final ObjectMapper objectMapper;
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

    public ResponseEntity<ApiResponse<?>> getAll(String lang, Boolean main, Boolean active, boolean onlyName)
    {
        if (onlyName && lang == null)
            throw new NotFoundException("When you need only name of categories, you must send language also");

        if (lang == null)
        {
            ApiResponse<List<Category>> response = new ApiResponse<>();

            if (main == null && active == null)
                response.setData(categoryRepository.findAll());
            else if (main == null)
                response.setData(categoryRepository.findAllByActive(active));
            else if (active == null)
                response.setData(categoryRepository.findAllByMain(main));
            else
                response.setData(categoryRepository.findAllByMainAndActive(main, active));

            response.setMessage(String.format("Found %s category(ies)", response.getData().size()));
            return ResponseEntity.ok(response);

        } else if (onlyName)
        {
            System.err.println("Only name ----------------- ");
            ApiResponse<List<CategoryNameDTO>> response = new ApiResponse<>();
            response.setData(new ArrayList<>());

            if (main == null && active == null)
            {
                List<Category> all = categoryRepository.findAll();
                all.forEach(i -> response.getData().add(new CategoryNameDTO(i, lang)));
            } else if (main == null)
            {
                List<Category> allByActive = categoryRepository.findAllByActive(active);
                allByActive.forEach(i -> response.getData().add(new CategoryNameDTO(i, lang)));
            } else if (active == null)
            {
                List<Category> allByMain = categoryRepository.findAllByMain(main);
                allByMain.forEach(i -> response.getData().add(new CategoryNameDTO(i, lang)));
            } else
            {
                List<Category> allByMainAndActive = categoryRepository.findAllByMainAndActive(main, active);
                allByMainAndActive.forEach(i -> response.getData().add(new CategoryNameDTO(i, lang)));
            }

            response.setMessage(String.format("Found %s category(ies)", response.getData().size()));
            return ResponseEntity.ok(response);

        } else
        {
            ApiResponse<List<CategoryDTO>> response = new ApiResponse<>();
            response.setData(new ArrayList<>());

            if (main == null && active == null)
            {
                List<Category> all = categoryRepository.findAll();
                all.forEach(i -> response.getData().add(new CategoryDTO(i, lang)));
            } else if (main == null)
            {
                List<Category> allByActive = categoryRepository.findAllByActive(active);
                allByActive.forEach(i -> response.getData().add(new CategoryDTO(i, lang)));
            } else if (active == null)
            {
                List<Category> allByMain = categoryRepository.findAllByMain(main);
                allByMain.forEach(i -> response.getData().add(new CategoryDTO(i, lang)));
            } else
            {
                List<Category> allByMainAndActive = categoryRepository.findAllByMainAndActive(main, active);
                allByMainAndActive.forEach(i -> response.getData().add(new CategoryDTO(i, lang)));
            }

            response.setMessage(String.format("Found %s category(ies)", response.getData().size()));
            return ResponseEntity.ok(response);
        }
    }

}
