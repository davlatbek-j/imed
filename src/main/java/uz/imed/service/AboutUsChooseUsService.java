package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.imed.entity.AboutUsChooseUs;
import uz.imed.entity.Certificate;
import uz.imed.exeptions.NotFoundException;
import uz.imed.payload.AboutUsChooseUsDTO;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.CertificateDTO;
import uz.imed.repository.AboutUsChooseUsRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsChooseUsService {


    private final AboutUsChooseUsRepository aboutUsChooseUsRepository;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<AboutUsChooseUs>> create(String json) {
        ApiResponse<AboutUsChooseUs> response = new ApiResponse<>();

        try {
            AboutUsChooseUs aboutUsChooseUs = objectMapper.readValue(json, AboutUsChooseUs.class);
            Long id = aboutUsChooseUsRepository.save(new AboutUsChooseUs()).getId();
            aboutUsChooseUs.setId(id);
            aboutUsChooseUs.setSlug(id + "-" + aboutUsChooseUs.getNameUz());
            AboutUsChooseUs saved = aboutUsChooseUsRepository.save(aboutUsChooseUs);
            response.setMessage("Successfully created");
            response.setData(saved);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    public ResponseEntity<ApiResponse<?>> getById(Long id, String lang) {
        if (lang == null) {
            ApiResponse<AboutUsChooseUs> response = new ApiResponse<>();
            Optional<AboutUsChooseUs> byId = aboutUsChooseUsRepository.findById(id);
            AboutUsChooseUs aboutUsChooseUs = byId.orElseThrow(() -> new NotFoundException("Data not found by id : " + id));
            response.setData(aboutUsChooseUs);
            response.setMessage("Found all data");
            return ResponseEntity.ok(response);
        }

        ApiResponse<AboutUsChooseUsDTO> response = new ApiResponse<>();
        Optional<AboutUsChooseUs> byId = aboutUsChooseUsRepository.findById(id);
        AboutUsChooseUs aboutUsChooseUs = byId.orElseThrow(() -> new NotFoundException("data not found by slug : " + id));
        response.setData(new AboutUsChooseUsDTO(aboutUsChooseUs, lang));
        response.setMessage("Found in language : " + lang);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<?>> findAll(String lang) {

        if (lang == null) {
            ApiResponse<List<AboutUsChooseUs>> response = new ApiResponse<>();
            List<AboutUsChooseUs> all = aboutUsChooseUsRepository.findAll();
            response.setMessage("Found " + all.size() + " data");
            response.setData(all);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApiResponse<List<AboutUsChooseUsDTO>> response = new ApiResponse<>();
        List<AboutUsChooseUs> all = aboutUsChooseUsRepository.findAll();
        response.setMessage("Found " + all.size() + " data");
        response.setData(new ArrayList<>());
        all.forEach(i -> response.getData().add(new AboutUsChooseUsDTO(i, lang)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<?>> get(String slug, String lang) {
        if (lang == null) {
            ApiResponse<AboutUsChooseUs> response = new ApiResponse<>();
            Optional<AboutUsChooseUs> bySlugIgnoreCase = aboutUsChooseUsRepository.findBySlugIgnoreCase(slug);
            AboutUsChooseUs certificate = bySlugIgnoreCase.orElseThrow(() -> new NotFoundException("Data not found by slug : " + slug));
            response.setData(certificate);
            response.setMessage("Found all data");
            return ResponseEntity.ok(response);
        }

        ApiResponse<AboutUsChooseUsDTO> response = new ApiResponse<>();
        Optional<AboutUsChooseUs> bySlugIgnoreCase = aboutUsChooseUsRepository.findBySlugIgnoreCase(slug);
        AboutUsChooseUs certificate = bySlugIgnoreCase.orElseThrow(() -> new NotFoundException("Data not found by slug : " + slug));
        response.setData(new AboutUsChooseUsDTO(certificate, lang));
        response.setMessage("Found in language : " + lang);
        return ResponseEntity.ok(response);

    }

    public ResponseEntity<ApiResponse<AboutUsChooseUs>> update(String json) {
        try {
            AboutUsChooseUs aboutUsChooseUs = objectMapper.readValue(json, AboutUsChooseUs.class);
            Long id = aboutUsChooseUs.getId();

            ApiResponse<AboutUsChooseUs> response = new ApiResponse<>();
            if (!aboutUsChooseUsRepository.existsById(id)) {
                response.setMessage("Data with id " + id + " does not exist");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            AboutUsChooseUs aboutUsChooseUs1 = aboutUsChooseUsRepository.findById(id).get();

            if (aboutUsChooseUs.getNameUz() != null) {
                aboutUsChooseUs1.setNameUz(aboutUsChooseUs.getNameUz());
                String slug = aboutUsChooseUs.getId() + "-" + SlugUtil.makeSlug(aboutUsChooseUs.getNameUz());
                aboutUsChooseUsRepository.updateSlug(slug, aboutUsChooseUs.getId());
                aboutUsChooseUs1.setSlug(slug);
            }
            if (aboutUsChooseUs.getNameRu() != null) {
                aboutUsChooseUs1.setNameRu(aboutUsChooseUs.getNameRu());
            }
            if (aboutUsChooseUs.getNameEng() != null) {
                aboutUsChooseUs1.setNameEng(aboutUsChooseUs.getNameEng());
            }
            if (aboutUsChooseUs.getDescriptionUz() != null) {
                aboutUsChooseUs1.setDescriptionUz(aboutUsChooseUs.getDescriptionUz());
            }
            if (aboutUsChooseUs.getDescriptionRu() != null) {
                aboutUsChooseUs1.setDescriptionRu(aboutUsChooseUs.getDescriptionRu());
            }
            if (aboutUsChooseUs.getDescriptionEng() != null) {
                aboutUsChooseUs1.setDescriptionUz(aboutUsChooseUs.getDescriptionEng());
            }


            aboutUsChooseUs1.setId(id);
            AboutUsChooseUs save = aboutUsChooseUsRepository.save(aboutUsChooseUs1);

            response.setMessage("Successfully updated");
            response.setData(save);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (aboutUsChooseUsRepository.findById(id).isEmpty()) {
            response.setMessage("AboutUsChooseUs is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        aboutUsChooseUsRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

}
