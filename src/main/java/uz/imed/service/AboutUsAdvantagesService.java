package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.*;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.AboutUsAdvantagesRepository;
import uz.imed.repository.PhotoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsAdvantagesService {
    private final AboutUsAdvantagesRepository aboutUsAdvantagesRepository;
    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<AboutUsAdvantages>> create(MultipartFile file) {
        ApiResponse<AboutUsAdvantages> response = new ApiResponse<>();
        AboutUsAdvantages aboutUsAdvantages = new AboutUsAdvantages();
        aboutUsAdvantages.setImageUrl(photoService.save(file).getHttpUrl());
        AboutUsAdvantages save = aboutUsAdvantagesRepository.save(aboutUsAdvantages);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<AboutUsAdvantages>> findById(Long id) {
        ApiResponse<AboutUsAdvantages> response = new ApiResponse<>();
        Optional<AboutUsAdvantages> optionalAboutUsAdvantages = aboutUsAdvantagesRepository.findById(id);
        if (optionalAboutUsAdvantages.isEmpty()) {
            response.setMessage("AboutUsChooseUs is not found by id");
            return ResponseEntity.status(404).body(response);
        }
        AboutUsAdvantages aboutUsAdvantages = optionalAboutUsAdvantages.get();
        response.setData(aboutUsAdvantages);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<AboutUsAdvantages>>> findAll() {
        ApiResponse<List<AboutUsAdvantages>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<AboutUsAdvantages> all = aboutUsAdvantagesRepository.findAll();
        all.forEach(aboutUsChooseUs -> response.getData().add(aboutUsChooseUs));
        response.setMessage("Found " + all.size() + " AboutUsChooseUs");
        return ResponseEntity.status(200).body(response);
    }



    public ResponseEntity<ApiResponse<AboutUsAdvantages>> update(Long id, MultipartFile newPhoto) {
        ApiResponse<AboutUsAdvantages> response = new ApiResponse<>();
        Optional<AboutUsAdvantages> optionalAboutUsPartnerTask = aboutUsAdvantagesRepository.findById(id);
        if (optionalAboutUsPartnerTask.isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        String oldPhotoUrl = aboutUsAdvantagesRepository.findPhotoUrlById(id);

        AboutUsAdvantages newAboutUsAdvantages = new AboutUsAdvantages();

        if (newPhoto == null || !(newPhoto.getSize() > 0)) {
            newAboutUsAdvantages.setImageUrl(oldPhotoUrl);
        }

            if (newPhoto != null && newPhoto.getSize() > 0) {
                Photo photo = photoService.save(newPhoto);
                newAboutUsAdvantages.setImageUrl(photo.getHttpUrl());
            }
            AboutUsAdvantages save = aboutUsAdvantagesRepository.save(newAboutUsAdvantages);
            response.setData(save);
            return ResponseEntity.status(201).body(response);

    }


    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (aboutUsAdvantagesRepository.findById(id).isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        aboutUsAdvantagesRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }
}
