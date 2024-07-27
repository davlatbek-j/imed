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

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<AboutUsPartner>> create(AboutUsPartner entity) {
        ApiResponse<AboutUsPartner> response = new ApiResponse<>();

        AboutUsPartner aboutUspartner = new AboutUsPartner();
        aboutUspartner.setName(entity.getName());
        aboutUspartner.setActive(entity.isActive());
        AboutUsPartner saved = aboutUsPartnerRepository.save(aboutUspartner);
        response.setMessage("Successfully created");
        response.setData(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse<AboutUsPartner>> uploadImage(Long id, MultipartFile photoFile){
        ApiResponse<AboutUsPartner> response = new ApiResponse<>();

        if (!(photoFile.getContentType().equals("image/png") ||
                photoFile.getContentType().equals("image/svg+xml"))) {
            response.setMessage("Invalid file , only image/png or image/svg+xml");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!aboutUsPartnerRepository.existsById(id)) {
            response.setMessage("Data with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        AboutUsPartner aboutUsPartner = aboutUsPartnerRepository.findById(id).get();
        response.setMessage("Found data with id " + id);
        aboutUsPartner.setIcon(photoService.save(photoFile));
        AboutUsPartner saved = aboutUsPartnerRepository.save(aboutUsPartner);
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

    public ResponseEntity<ApiResponse<AboutUsPartner>> update(Long id, AboutUsPartner entity) {
        ApiResponse<AboutUsPartner> response = new ApiResponse<>();
        Optional<AboutUsPartner> optionalAboutUsPartner = aboutUsPartnerRepository.findAll().stream().findFirst();
        if (optionalAboutUsPartner.isEmpty()) {
            response.setMessage("AboutUsHeader is not found");
            return ResponseEntity.status(404).body(response);
        }
        AboutUsPartner newentity = aboutUsPartnerRepository.findById(id).get();

        if (entity.getName() != null ) {
            newentity.setName(entity.getName());
        }
        newentity.setId(id);
        AboutUsPartner save = aboutUsPartnerRepository.save(newentity);

        response.setMessage("Successfully updated");
        response.setData(save);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public ResponseEntity<ApiResponse<AboutUsPartner>> updateIcon(Long id,MultipartFile icon){
        ApiResponse<AboutUsPartner> response = new ApiResponse<>();

        if (!(icon.getContentType().equals("image/png") ||
                icon.getContentType().equals("image/svg+xml"))) {
            response.setMessage("Invalid file , only image/png or image/svg+xml");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!aboutUsPartnerRepository.existsById(id)) {
            response.setMessage("Data with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        AboutUsPartner aboutUsPartner = aboutUsPartnerRepository.findById(id).get();
        response.setMessage("Found data with id " + id);
        aboutUsPartner.setIcon(photoService.save(icon));
        AboutUsPartner saved = aboutUsPartnerRepository.save(aboutUsPartner);
        response.setMessage("Successfully created");
        response.setData(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
