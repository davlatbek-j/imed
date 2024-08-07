package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.*;
import uz.imed.exeptions.NotFoundException;
import uz.imed.payload.AboutUsHeaderDTO;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.CertificateDTO;
import uz.imed.payload.ClientDTO;
import uz.imed.repository.CertificateRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificateService {
    private final CertificateRepository certificateRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<Certificate>> create(String json,MultipartFile file) {
        ApiResponse<Certificate> response = new ApiResponse<>();
        try {
            Certificate certificate = objectMapper.readValue(json, Certificate.class);
            Long id = certificateRepository.save(new Certificate()).getId();
            certificate.setCertificateImage(photoService.save(file));
            certificate.setId(id);
            certificate.setSlug(id + "-" + certificate.getTitleUz());
            ArrayList<Photo> objects = new ArrayList<>();

            response.setData(certificateRepository.save(certificate));
            response.setMessage("Certificate succesfully created");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse json: " + e.getMessage());
        }
    }

    public ResponseEntity<ApiResponse<?>> getById(Long id, String lang) {
        if (lang == null) {
            ApiResponse<Certificate> response = new ApiResponse<>();
            Optional<Certificate> byId = certificateRepository.findById(id);
            Certificate aboutUsHeader = byId.orElseThrow(() -> new NotFoundException("Certificate not found by id : " + id));
            response.setData(aboutUsHeader);
            response.setMessage("Found all data");
            return ResponseEntity.ok(response);
        }

        ApiResponse<CertificateDTO> response = new ApiResponse<>();
        Optional<Certificate> byId = certificateRepository.findById(id);
        Certificate aboutUsHeader = byId.orElseThrow(() -> new NotFoundException("Certificate not found by slug : " + id));
        response.setData(new CertificateDTO(aboutUsHeader, lang));
        response.setMessage("Found in language : " + lang);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> findAll(String lang) {
        if (lang == null) {
            ApiResponse<List<Certificate>> response = new ApiResponse<>();
            List<Certificate> all = certificateRepository.findAll();
            response.setMessage("Found " + all.size() + " data");
            response.setData(all);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApiResponse<List<CertificateDTO>> response = new ApiResponse<>();
        List<Certificate> all = certificateRepository.findAll();
        response.setMessage("Found " + all.size() + " data");
        response.setData(new ArrayList<>());
        all.forEach(i -> response.getData().add(new CertificateDTO(i, lang)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    public ResponseEntity<ApiResponse<?>> get(String slug, String lang) {
        if (lang == null) {
            ApiResponse<Certificate> response = new ApiResponse<>();
            Optional<Certificate> bySlugIgnoreCase = certificateRepository.findBySlugIgnoreCase(slug);
            Certificate certificate = bySlugIgnoreCase.orElseThrow(() -> new NotFoundException("Certificate not found by slug : " + slug));
            response.setData(certificate);
            response.setMessage("Found all certificate(s)");
            return ResponseEntity.ok(response);
        }

        ApiResponse<CertificateDTO> response = new ApiResponse<>();
        Optional<Certificate> bySlugIgnoreCase = certificateRepository.findBySlugIgnoreCase(slug);
        Certificate certificate = bySlugIgnoreCase.orElseThrow(() -> new NotFoundException("Certificate not found by slug : " + slug));
        response.setData(new CertificateDTO(certificate, lang));
        response.setMessage("Found in language : " + lang);
        return ResponseEntity.ok(response);

    }

    public ResponseEntity<ApiResponse<Certificate>> update(String json) {
        try {
            Certificate certificate = objectMapper.readValue(json, Certificate.class);
            Long id = certificate.getId();

            ApiResponse<Certificate> response = new ApiResponse<>();
            if (!certificateRepository.existsById(id)) {
                response.setMessage("Certificate with id " + id + " does not exist");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Certificate certificate1 = certificateRepository.findById(id).get();

            if (certificate.getTitleUz() != null ) {
                certificate1.setTitleUz(certificate.getTitleUz());
                String slug = certificate.getId() + "-" + SlugUtil.makeSlug(certificate.getTitleUz());
                certificateRepository.updateSlug(slug, certificate.getId());
                certificate1.setSlug(slug);
            }
            if (certificate.getTitleRu() != null ) {
                certificate1.setTitleRu(certificate.getTitleRu());
            }
            if (certificate.getTitleEng() != null ) {
                certificate1.setTitleEng(certificate.getTitleEng());;
            }
            if (certificate.getDescriptionUz() != null ) {
                certificate1.setDescriptionUz(certificate.getDescriptionUz());
            }
            if (certificate.getDescriptionRu() != null ) {
                certificate1.setDescriptionRu(certificate.getDescriptionRu());
            }
            if (certificate.getDescriptionEng() != null ) {
                certificate1.setDescriptionUz(certificate.getDescriptionEng());
            }


            certificate1.setId(id);
            Certificate save = certificateRepository.save(certificate1);

            response.setMessage("Successfully updated");
            response.setData(save);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<ApiResponse<Certificate>> delete(Long id) {
        ApiResponse<Certificate> response = new ApiResponse<>();
        try {
            certificateRepository.deleteById(id);
            response.setMessage("Successfully deleted");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage("Error deleting client" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
