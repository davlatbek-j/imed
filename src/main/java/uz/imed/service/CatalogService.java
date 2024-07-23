package uz.imed.service;



import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.imed.entity.Catalog;
import uz.imed.entity.EquipmentCategory;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.CatalogDTO;
import uz.imed.repository.CatalogRepository;
import uz.imed.repository.EquipmentCategoryRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;

    private final EquipmentCategoryRepository categoryRepository;

    public ResponseEntity<ApiResponse<CatalogDTO>> create(Long categoryId, Catalog catalog) {
        ApiResponse<CatalogDTO> response = new ApiResponse<>();
        Optional<EquipmentCategory> optionalEquipmentCategory = categoryRepository.findById(categoryId);
        if (optionalEquipmentCategory.isEmpty()) {
            response.setMessage("Category is not found by id: " + categoryId);
            return ResponseEntity.status(404).body(response);
        }

        // Katalog mavjudligini tekshirish
        Optional<Catalog> existingCatalog = catalogRepository.findByNameAndCategory_Id(catalog.getName(), categoryId);
        if (existingCatalog.isPresent()) {
            response.setMessage("Catalog is already exists by name: " + catalog.getName() + " in " + existingCatalog.get().getCategory().getName() + " category");
            return ResponseEntity.status(401).body(response);
        }

        catalog.setActive(true);
        catalog.setCategory(optionalEquipmentCategory.get());
        Catalog save = catalogRepository.save(catalog);
        String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getName());
        catalogRepository.updateSlug(slug, save.getId());
        save.setSlug(slug);
        response.setData(new CatalogDTO(save));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<CatalogDTO>> findById(Long id) {
        ApiResponse<CatalogDTO> response = new ApiResponse<>();
        Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if (optionalCatalog.isEmpty()) {
            response.setMessage("Catalog is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Catalog catalog = optionalCatalog.get();
        response.setData(new CatalogDTO(catalog));
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<CatalogDTO>> findBySlug(String slug) {
        ApiResponse<CatalogDTO> response = new ApiResponse<>();
        Optional<Catalog> optionalCatalog = catalogRepository.findBySlug(slug);
        if (optionalCatalog.isEmpty()) {
            response.setMessage("Catalog is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        Catalog catalog = optionalCatalog.get();
        response.setMessage("Found");
        response.setData(new CatalogDTO(catalog));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<CatalogDTO>>> findAll() {
        ApiResponse<List<CatalogDTO>> response = new ApiResponse<>();
        List<Catalog> all = catalogRepository.findAll();
        response.setData(new ArrayList<>());
        all.forEach(catalog -> response.getData().add(new CatalogDTO(catalog)));
        response.setMessage("Found " + all.size() + " catalog(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<CatalogDTO>> update(Long id, Catalog catalog) {
        ApiResponse<CatalogDTO> response = new ApiResponse<>();
        Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if (optionalCatalog.isEmpty()) {
            response.setMessage("Catalog is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Catalog oldCatalog = optionalCatalog.get();
        Optional<Catalog> existingCatalog = catalogRepository.findByNameAndCategory_Id(catalog.getName(), oldCatalog.getCategory().getId());
        if (existingCatalog.isPresent()) {
            response.setMessage("Catalog is already exists by name: " + catalog.getName() + " in " + existingCatalog.get().getCategory().getName() + " category");
            return ResponseEntity.status(401).body(response);
        }
        oldCatalog.setSlug(oldCatalog.getSlug());
        oldCatalog.setActive(oldCatalog.isActive());
        oldCatalog.setCategory(oldCatalog.getCategory());
        oldCatalog.setName(catalog.getName());
        Catalog save = catalogRepository.save(oldCatalog);
        response.setData(new CatalogDTO(save));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (catalogRepository.findById(id).isEmpty()) {
            response.setMessage("Catalog is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        catalogRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if (optionalCatalog.isEmpty()) {
            response.setMessage("Catalog id not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Catalog catalog = optionalCatalog.get();
        boolean active = !catalog.isActive();
        catalogRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current catalog active: " + active);
        return ResponseEntity.status(200).body(response);
    }

}
