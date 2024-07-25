package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.New;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.NewDTO;
import uz.imed.repository.NewRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NewService {

    private final NewRepository newRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<New>> create(String strNew, MultipartFile mainPhoto, List<MultipartFile> photoFiles) {

        ApiResponse<New> response = new ApiResponse<>();
        try {
            New newness = objectMapper.readValue(strNew, New.class);
            newness.setPhotoUrls(new ArrayList<>());
            newness.setActive(true);
            newness.setMainPhotoUrl(photoService.save(mainPhoto).getHttpUrl());
            for (MultipartFile photo : photoFiles) {
                newness.getPhotoUrls().add(photoService.save(photo).getHttpUrl());
            }
            New save = newRepository.save(newness);
            String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getTitle());
            newRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<New>> findById(Long id) {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findById(id);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        response.setMessage("Found");
        response.setData(newness);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<New>> findBySlug(String slug) {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findBySlug(slug);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        response.setMessage("Found");
        response.setData(newness);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> findAll() {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        List<New> all = newRepository.findAll();
        response.setData(new ArrayList<>());
        all.forEach(newness -> response.getData().add(new NewDTO(newness)));
        response.setMessage("Found " + all.size() + " new(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Page<NewDTO>>> findAllByPageNation(int page, int size) {
        ApiResponse<Page<NewDTO>> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<New> all = newRepository.findAll(pageable);
        Page<NewDTO> map = all.map(NewDTO::new);
        response.setData(map);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> findFourNews() {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        List<New> all = newRepository.findAllByOrderByIdAsc();
        response.setData(new ArrayList<>());
        all.stream().limit(4).toList().forEach(newness -> response.getData().add(new NewDTO(newness)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> findOtherFourNews(String newSlug) {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        List<New> all = newRepository.findAllByOrderByIdAsc();
        response.setData(new ArrayList<>());
        all.stream()
                .filter(newness -> !newness.getSlug().equals(newSlug))
                .limit(4).toList()
                .forEach(newness -> response.getData().add(new NewDTO(newness)));
        return ResponseEntity.status(200).body(response);
    }


    public ResponseEntity<ApiResponse<New>> update(Long id, String newJson, MultipartFile newMainPhoto, List<MultipartFile> newPhotosFile) {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findById(id);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        List<String> oldPhotoUrls = optionalNew.get().getPhotoUrls();
        String oldMainPhotoUrl = optionalNew.get().getMainPhotoUrl();
        boolean active = optionalNew.get().isActive();
        String slug = newRepository.findSlugById(id);
        New newness = new New();

        try {
            if (newJson != null) {
                newness = objectMapper.readValue(newJson, New.class);
                newness.setId(id);
                newness.setSlug(slug);
                newness.setActive(active);
            } else {
                newness = newRepository.findById(id).get();
            }

            if (newMainPhoto == null || newMainPhoto.isEmpty()) {
                newness.setMainPhotoUrl(oldMainPhotoUrl);
            } else {
                newness.setMainPhotoUrl(photoService.save(newMainPhoto).getHttpUrl());
            }
            if (newPhotosFile == null || newPhotosFile.isEmpty()) {
                newness.setPhotoUrls(oldPhotoUrls);
            } else {
                newness.setPhotoUrls(new ArrayList<>());
                for (MultipartFile photo : newPhotosFile) {
                    newness.getPhotoUrls().add(photoService.save(photo).getHttpUrl());
                }
            }

            New save = newRepository.save(newness);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (newRepository.findById(id).isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        newRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findById(id);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        boolean active = !newness.isActive();
        newRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current new active: " + active);
        return ResponseEntity.status(200).body(response);
    }
}
