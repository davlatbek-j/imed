package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.AboutUsChooseUs;
import uz.imed.entity.AboutUsHeader;
import uz.imed.exeptions.NotFoundException;
import uz.imed.payload.AboutUsChooseUsDTO;
import uz.imed.payload.AboutUsHeaderDTO;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.AboutUsHeaderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsHeaderService {

    private final AboutUsHeaderRepository aboutUsHeaderRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<AboutUsHeader>> create(String json,MultipartFile file) {
        ApiResponse<AboutUsHeader> response = new ApiResponse<>();

        try {
            AboutUsHeader aboutUsHeader = objectMapper.readValue(json, AboutUsHeader.class);
            Long id = aboutUsHeaderRepository.save(new AboutUsHeader()).getId();
            aboutUsHeader.setId(id);
            aboutUsHeader.setPhoto(photoService.save(file));
            aboutUsHeader.setSlug(id + "-" + aboutUsHeader.getTitleUz());
            AboutUsHeader saved = aboutUsHeaderRepository.save(aboutUsHeader);
            response.setMessage("Successfully created");
            response.setData(saved);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<ApiResponse<?>> getById(Long id, String lang) {
        if (lang == null) {
            ApiResponse<AboutUsHeader> response = new ApiResponse<>();
            Optional<AboutUsHeader> byId = aboutUsHeaderRepository.findById(id);
            AboutUsHeader aboutUsHeader = byId.orElseThrow(() -> new NotFoundException("Data not found by id : " + id));
            response.setData(aboutUsHeader);
            response.setMessage("Found all data");
            return ResponseEntity.ok(response);
        }

        ApiResponse<AboutUsHeaderDTO> response = new ApiResponse<>();
        Optional<AboutUsHeader> byId = aboutUsHeaderRepository.findById(id);
        AboutUsHeader aboutUsHeader = byId.orElseThrow(() -> new NotFoundException("data not found by slug : " + id));
        response.setData(new AboutUsHeaderDTO(aboutUsHeader, lang));
        response.setMessage("Found in language : " + lang);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> findAll(String lang) {

        if (lang == null) {
            ApiResponse<List<AboutUsHeader>> response = new ApiResponse<>();
            List<AboutUsHeader> all = aboutUsHeaderRepository.findAll();
            response.setMessage("Found " + all.size() + " data");
            response.setData(all);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApiResponse<List<AboutUsHeaderDTO>> response = new ApiResponse<>();
        List<AboutUsHeader> all = aboutUsHeaderRepository.findAll();
        response.setMessage("Found " + all.size() + " data");
        response.setData(new ArrayList<>());
        all.forEach(i -> response.getData().add(new AboutUsHeaderDTO(i, lang)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<?>> get(String slug, String lang) {
        if (lang == null) {
            ApiResponse<AboutUsHeader> response = new ApiResponse<>();
            Optional<AboutUsHeader> bySlugIgnoreCase = aboutUsHeaderRepository.findBySlugIgnoreCase(slug);
            AboutUsHeader certificate = bySlugIgnoreCase.orElseThrow(() -> new NotFoundException("Data not found by slug : " + slug));
            response.setData(certificate);
            response.setMessage("Found all data");
            return ResponseEntity.ok(response);
        }

        ApiResponse<AboutUsHeaderDTO> response = new ApiResponse<>();
        Optional<AboutUsHeader> bySlugIgnoreCase = aboutUsHeaderRepository.findBySlugIgnoreCase(slug);
        AboutUsHeader certificate = bySlugIgnoreCase.orElseThrow(() -> new NotFoundException("Data not found by slug : " + slug));
        response.setData(new AboutUsHeaderDTO(certificate, lang));
        response.setMessage("Found in language : " + lang);
        return ResponseEntity.ok(response);

    }


    public ResponseEntity<ApiResponse<AboutUsHeader>> update(String json) {


        try {
            AboutUsHeader newAboutUsHeader = objectMapper.readValue(json, AboutUsHeader.class);
            Long id = newAboutUsHeader.getId();

            ApiResponse<AboutUsHeader> response = new ApiResponse<>();
            if (!aboutUsHeaderRepository.existsById(id)) {
                response.setMessage("Data with id " + id + " does not exist");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            AboutUsHeader newentity = aboutUsHeaderRepository.findById(id).get();

        if (newAboutUsHeader.getTitleUz() != null ) {
            newentity.setTitleUz(newAboutUsHeader.getTitleUz());
        }
        if (newAboutUsHeader.getTitleRu() != null ) {
            newentity.setTitleRu(newAboutUsHeader.getTitleRu());
        }
        if (newAboutUsHeader.getTitleEng() != null ) {
            newentity.setTitleUz(newAboutUsHeader.getTitleUz());
        }
        if (newAboutUsHeader.getFormName() != null ) {
            newentity.setFormName(newAboutUsHeader.getFormName());
        }
        if (newAboutUsHeader.getSubtitleUz() != null  ) {
            newentity.setSubtitleUz(newAboutUsHeader.getSubtitleUz());
        }
        if (newAboutUsHeader.getSubtitleRu() != null ) {
            newentity.setSubtitleRu(newAboutUsHeader.getSubtitleRu());
        }
        if (newAboutUsHeader.getSubtitleEng() != null ) {
            newentity.setSubtitleEng(newAboutUsHeader.getSubtitleEng());
        }

        if (newAboutUsHeader.getDescriptionUz() != null ) {
            newentity.setDescriptionUz(newAboutUsHeader.getDescriptionUz());
        }
        if (newAboutUsHeader.getDescriptionRu() != null ) {
            newentity.setDescriptionRu(newAboutUsHeader.getDescriptionRu());
        }
        if (newAboutUsHeader.getDescriptionEng() != null ) {
            newentity.setDescriptionEng(newAboutUsHeader.getDescriptionEng());
        }


        newentity.setId(id);
        AboutUsHeader save = aboutUsHeaderRepository.save(newentity);

        response.setMessage("Successfully updated");
        response.setData(save);
        return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<AboutUsHeader> optionalAboutUsHeader = aboutUsHeaderRepository.findAll().stream().findFirst();
        if (optionalAboutUsHeader.isEmpty()) {
            response.setMessage("AboutUsHeader is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optionalAboutUsHeader.get().getId();
        aboutUsHeaderRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }


}
