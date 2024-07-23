package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.EquipmentCategory;
import uz.imed.entity.Photo;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.EquipmentCategoryRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipmentCategoryService {

    private final EquipmentCategoryRepository equipmentCategoryRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<EquipmentCategory>> create(String json, MultipartFile photoFile) {
        ApiResponse<EquipmentCategory> response = new ApiResponse<>();
        try {
            EquipmentCategory equipmentCategory = objectMapper.readValue(json, EquipmentCategory.class);
            if (equipmentCategoryRepository.findByName(equipmentCategory.getName()).isPresent()){
                response.setMessage("This category is already exists by name: "+equipmentCategory.getName());
                return ResponseEntity.status(401).body(response);
            }
            Photo photo = photoService.save(photoFile);
            equipmentCategory.setPhotoUrl(photo.getHttpUrl());
            equipmentCategory.setActive(true);
            EquipmentCategory save = equipmentCategoryRepository.save(equipmentCategory);
            String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getName());
            equipmentCategoryRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    public ResponseEntity<ApiResponse<EquipmentCategory>> findById(Long id) {
        ApiResponse<EquipmentCategory> response = new ApiResponse<>();
        Optional<EquipmentCategory> optionalEquipmentCategory = equipmentCategoryRepository.findById(id);
        if (optionalEquipmentCategory.isEmpty()) {
            response.setMessage("Category is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        EquipmentCategory equipmentCategory = optionalEquipmentCategory.get();
        response.setMessage("Found");
        response.setData(equipmentCategory);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<EquipmentCategory>> findBySlug(String slug) {
        ApiResponse<EquipmentCategory> response = new ApiResponse<>();
        Optional<EquipmentCategory> optionalEquipmentCategory = equipmentCategoryRepository.findBySlug(slug);
        if (optionalEquipmentCategory.isEmpty()) {
            response.setMessage("Category is not found");
            return ResponseEntity.status(404).body(response);
        }
        EquipmentCategory equipmentCategory = optionalEquipmentCategory.get();
        response.setData(equipmentCategory);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<EquipmentCategory>>> findAll() {
        ApiResponse<List<EquipmentCategory>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<EquipmentCategory> all = equipmentCategoryRepository.findAll();
        all.forEach(equipmentCategory -> response.getData().add(equipmentCategory));
        response.setMessage("Found " + all.size() + " categories");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<EquipmentCategory>> update(Long id, String newJson, MultipartFile newPhoto) {
        ApiResponse<EquipmentCategory> response = new ApiResponse<>();
        Optional<EquipmentCategory> optionalEquipmentCategory = equipmentCategoryRepository.findById(id);
        if (optionalEquipmentCategory.isEmpty()) {
            response.setMessage("Category is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        String oldPhotoUrl = equipmentCategoryRepository.findPhotoUrlById(id);
        String slug = equipmentCategoryRepository.findSlugById(id);
        boolean active = optionalEquipmentCategory.get().isActive();
        EquipmentCategory newCategory = new EquipmentCategory();

        try {
            if (newJson != null) {
                newCategory = objectMapper.readValue(newJson, EquipmentCategory.class);
                if (equipmentCategoryRepository.findByName(newCategory.getName()).isPresent()){
                    response.setMessage("This category is already exists by name: "+newCategory.getName());
                    return ResponseEntity.status(401).body(response);
                }
                if (newPhoto == null || newPhoto.isEmpty()) {
                    newCategory.setPhotoUrl(oldPhotoUrl);
                }
                newCategory.setId(id);
                newCategory.setSlug(slug);
                newCategory.setActive(active);
            } else {
                newCategory = equipmentCategoryRepository.findById(id).get();
            }

            if (newPhoto != null && !newPhoto.isEmpty()) {
                Photo photo = photoService.save(newPhoto);
                newCategory.setPhotoUrl(photo.getHttpUrl());
            }
            EquipmentCategory save = equipmentCategoryRepository.save(newCategory);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<EquipmentCategory> response = new ApiResponse<>();
        Optional<EquipmentCategory> optionalEquipmentCategory = equipmentCategoryRepository.findById(id);
        if (optionalEquipmentCategory.isEmpty()) {
            response.setMessage("Category is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        EquipmentCategory equipmentCategory = optionalEquipmentCategory.get();
        boolean active = !equipmentCategory.isActive();
        equipmentCategoryRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current category active: " + active);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (equipmentCategoryRepository.findById(id).isEmpty()) {
            response.setMessage("Category is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        equipmentCategoryRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }


}