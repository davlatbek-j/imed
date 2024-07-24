package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.AboutUsPartner;
import uz.imed.entity.Photo;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.AboutUsPartnerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AboutUsPartnerService {


    private final AboutUsPartnerRepository aboutUsPartnerRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<AboutUsPartner>> create(String json, MultipartFile photoFile) {
        ApiResponse<AboutUsPartner> response = new ApiResponse<>();
        try {
            AboutUsPartner aboutUsPartnerTask = objectMapper.readValue(json, AboutUsPartner.class);
            Photo photo = photoService.save(photoFile);
            aboutUsPartnerTask.setIconUrl(photo.getHttpUrl());
            aboutUsPartnerTask.setActive(true);
            AboutUsPartner save = aboutUsPartnerRepository.save(aboutUsPartnerTask);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<AboutUsPartner>> findById(Long id) {
        ApiResponse<AboutUsPartner> response = new ApiResponse<>();
        Optional<AboutUsPartner> optionalAboutUsPartnerTask = aboutUsPartnerRepository.findById(id);
        if (optionalAboutUsPartnerTask.isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        AboutUsPartner aboutUsPartnerTask = optionalAboutUsPartnerTask.get();
        response.setData(aboutUsPartnerTask);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<AboutUsPartner>>> findAll() {
        ApiResponse<List<AboutUsPartner>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<AboutUsPartner> all = aboutUsPartnerRepository.findAll();
        all.forEach(aboutUsPartnerTask -> response.getData().add(aboutUsPartnerTask));
        response.setMessage("Found " + all.size() + " AboutUsPartnerTask");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<AboutUsPartner>> update(Long id, String newJson, MultipartFile newPhoto) {
        ApiResponse<AboutUsPartner> response = new ApiResponse<>();
        Optional<AboutUsPartner> optionalAboutUsPartnerTask = aboutUsPartnerRepository.findById(id);
        if (optionalAboutUsPartnerTask.isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        String oldPhotoUrl = aboutUsPartnerRepository.findPhotoUrlById(id);
        boolean active = optionalAboutUsPartnerTask.get().isActive();
        AboutUsPartner newAboutUsPartnerTask = new AboutUsPartner();

        try {
            if (newJson != null) {
                newAboutUsPartnerTask = objectMapper.readValue(newJson, AboutUsPartner.class);
                if (newPhoto == null || !(newPhoto.getSize() > 0)) {
                    newAboutUsPartnerTask.setIconUrl(oldPhotoUrl);
                }
                newAboutUsPartnerTask.setId(id);
                newAboutUsPartnerTask.setActive(active);
            } else {
                newAboutUsPartnerTask = aboutUsPartnerRepository.findById(id).get();
            }

            if (newPhoto != null && newPhoto.getSize() > 0) {
                Photo photo = photoService.save(newPhoto);
                newAboutUsPartnerTask.setIconUrl(photo.getHttpUrl());
            }
            AboutUsPartner save = aboutUsPartnerRepository.save(newAboutUsPartnerTask);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (aboutUsPartnerRepository.findById(id).isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        aboutUsPartnerRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<AboutUsPartner> optionalAboutUsPartnerTask = aboutUsPartnerRepository.findById(id);
        if (optionalAboutUsPartnerTask.isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        AboutUsPartner aboutUsPartnerTask = optionalAboutUsPartnerTask.get();
        boolean active = !aboutUsPartnerTask.isActive();
        aboutUsPartnerRepository.changeActive(id, active);
        response.setMessage("AboutUsPartnerTask active: " + active);
        return ResponseEntity.status(200).body(response);
    }

}
