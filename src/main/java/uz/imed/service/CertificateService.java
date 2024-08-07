package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Certificate;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.CertificateDTO;
import uz.imed.payload.CertificatePhotoDTO;
import uz.imed.repository.CertificateRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Certificate>> create(String json, MultipartFile photoFile) {
        ApiResponse<Certificate> response = new ApiResponse<>();
        try {
            Certificate certificate = objectMapper.readValue(json, Certificate.class);
            certificate.setActive(true);
            certificate.setPhoto(photoService.save(photoFile));
            Certificate saved = certificateRepository.save(certificate);
            String slug = saved.getId() + "-" + SlugUtil.makeSlug(saved.getTitleUz());
            certificateRepository.updateSlug(slug, saved.getId());
            saved.setSlug(slug);
            response.setData(saved);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> findBySlug(String slug, String lang) {
        Certificate certificate = certificateRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Certificate is not found by slug: " + slug));
        if (lang == null) {
            ApiResponse<Certificate> response = new ApiResponse<>();
            response.setData(certificate);
            return ResponseEntity.ok(response);
        }
        ApiResponse<CertificateDTO> response = new ApiResponse<>();
        response.setData(new CertificateDTO(certificate, lang));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> findAll(String lang, Boolean onlyPhoto) {
        List<Certificate> all = certificateRepository.findAll();
        if (lang == null) {
            ApiResponse<List<Certificate>> response = new ApiResponse<>();
            response.setData(new ArrayList<>());
            all.forEach(certificate -> response.getData().add(certificate));
            response.setMessage("Found " + all.size() + " certificate(s)");
            return ResponseEntity.ok(response);
        }
        if (onlyPhoto){
            ApiResponse<List<CertificatePhotoDTO>> response=new ApiResponse<>();
            response.setData(new ArrayList<>());
            all.forEach(certificate -> response.getData().add(new CertificatePhotoDTO(certificate)));
            return ResponseEntity.ok(response);
        }
        ApiResponse<List<CertificateDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        all.forEach(certificate -> response.getData().add(new CertificateDTO(certificate, lang)));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Certificate>> update(Certificate certificate) {
        ApiResponse<Certificate> response = new ApiResponse<>();
        Certificate fromDB = certificateRepository.findById(certificate.getId())
                .orElseThrow(() -> new NotFoundException("Certificate is not found by id: " + certificate.getId()));

        if (certificate.getTitleUz() != null) {
            fromDB.setTitleUz(certificate.getTitleUz());
            fromDB.setSlug(fromDB.getId() + "-" + SlugUtil.makeSlug(certificate.getTitleUz()));
        }
        if (certificate.getTitleEn() != null) {
            fromDB.setTitleEn(certificate.getTitleEn());
        }
        if (certificate.getTitleRu() != null) {
            fromDB.setTitleRu(certificate.getTitleRu());
        }

        if (certificate.getTextUz() != null) {
            fromDB.setTextUz(certificate.getTextUz());
        }
        if (certificate.getTextEn() != null) {
            fromDB.setTextEn(certificate.getTextEn());
        }
        if (certificate.getTextRu() != null) {
            fromDB.setTextRu(certificate.getTextRu());
        }

        response.setData(certificateRepository.save(fromDB));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!certificateRepository.existsById(id)) {
            throw new NotFoundException("Certificate is not found by id: " + id);
        }
        certificateRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }

}
