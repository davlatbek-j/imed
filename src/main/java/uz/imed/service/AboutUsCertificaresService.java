package uz.imed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.*;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.AboutUsCertificatesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsCertificaresService {
    private final AboutUsCertificatesRepository aboutUsCertificatesRepository;
    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<AboutUsCertificates>> create(MultipartFile file) {
        ApiResponse<AboutUsCertificates> response = new ApiResponse<>();
        AboutUsCertificates aBoutUsCertificates=new AboutUsCertificates();
        aBoutUsCertificates.setCertificateImage(photoService.save(file));

        AboutUsCertificates save = aboutUsCertificatesRepository.save(aBoutUsCertificates);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<AboutUsCertificates>> findById(Long id) {
        ApiResponse<AboutUsCertificates> response = new ApiResponse<>();
        Optional<AboutUsCertificates> optionalAboutUsAdvantages = aboutUsCertificatesRepository.findById(id);
        if (optionalAboutUsAdvantages.isEmpty()) {
            response.setMessage("AboutUsChooseUs is not found by id");
            return ResponseEntity.status(404).body(response);
        }
        AboutUsCertificates aBoutUsCertificates = optionalAboutUsAdvantages.get();
        response.setData(aBoutUsCertificates);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<AboutUsCertificates>>> findAll() {
        ApiResponse<List<AboutUsCertificates>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<AboutUsCertificates> all = aboutUsCertificatesRepository.findAll();
        all.forEach(aboutUsChooseUs -> response.getData().add(aboutUsChooseUs));
        response.setMessage("Found " + all.size() + " AboutUsChooseUs");
        return ResponseEntity.status(200).body(response);
    }



    public ResponseEntity<ApiResponse<AboutUsCertificates>> update(Long id, MultipartFile newPhoto) {
        ApiResponse<AboutUsCertificates> response = new ApiResponse<>();
        Optional<AboutUsCertificates> optionalABoutUsCertificates = aboutUsCertificatesRepository.findById(id);
        if (optionalABoutUsCertificates.isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }


        AboutUsCertificates newAboutUsCertificates = new AboutUsCertificates();

        if (newPhoto != null || !(newPhoto.getSize() > 0)) {
            newAboutUsCertificates.setCertificateImage(photoService.save(newPhoto));
        }


        AboutUsCertificates save = aboutUsCertificatesRepository.save(newAboutUsCertificates);
        response.setData(save);
            return ResponseEntity.status(201).body(response);

    }


    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (aboutUsCertificatesRepository.findById(id).isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        aboutUsCertificatesRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }
}
