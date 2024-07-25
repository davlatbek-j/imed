package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.AboutUsHeader;
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

    public ResponseEntity<ApiResponse<AboutUsPartner>> create(AboutUsPartner entity, MultipartFile photoFile) {
        ApiResponse<AboutUsPartner> response = new ApiResponse<>();

        AboutUsPartner aboutUspartner = new AboutUsPartner();
        aboutUspartner.setIcon(photoService.save(photoFile));
        aboutUspartner.setName(entity.getName());
        aboutUspartner.setActive(entity.isActive());
        AboutUsPartner saved = aboutUsPartnerRepository.save(aboutUspartner);
        response.setMessage("Successfully created");
        response.setData(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    public ResponseEntity<ApiResponse<AboutUsPartner>> update(Long id, AboutUsPartner entity, MultipartFile newPhoto) {
        AboutUsPartner aboutUsPartner = aboutUsPartnerRepository.findById(id).get();
        ApiResponse<AboutUsPartner> response = new ApiResponse<>();

        if (aboutUsPartner == null) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }

        AboutUsPartner newAboutUsPartnerTask = new AboutUsPartner();
        if (entity.getName() != null || !entity.getName().isEmpty()) {
            aboutUsPartner.setName(entity.getName());
        }
        if (newPhoto != null && newPhoto.getSize() > 0) {
            Photo photo = photoService.save(newPhoto);
            newAboutUsPartnerTask.setIcon(photo);
        }
        AboutUsPartner save = aboutUsPartnerRepository.save(newAboutUsPartnerTask);
        response.setData(save);
        return ResponseEntity.status(201).body(response);

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
