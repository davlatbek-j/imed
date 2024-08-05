package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.AboutUsPageSection;
import uz.imed.entity.AboutUsSectionOption;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.AboutUsPageSectionDTO;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.AboutUsPageSectionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsPageSectionService {

    private final AboutUsPageSectionRepository aboutUsPageSectionRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<AboutUsPageSection>> create(String json, MultipartFile photoFile) {
        ApiResponse<AboutUsPageSection> response = new ApiResponse<>();
        Optional<AboutUsPageSection> firstPageSection = aboutUsPageSectionRepository.findAll().stream().findFirst();

        try {
            AboutUsPageSection pageSection = objectMapper.readValue(json, AboutUsPageSection.class);
            if (firstPageSection.isPresent()) {
                pageSection.setId(firstPageSection.get().getId());
                return update(pageSection);
            }
            pageSection.setPhoto(photoService.save(photoFile));
            AboutUsPageSection save = aboutUsPageSectionRepository.save(pageSection);
            response.setData(save);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> find(String lang) {
        AboutUsPageSection pageSection = aboutUsPageSectionRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new NotFoundException("PageSection is not found"));
        if (lang != null) {
            ApiResponse<AboutUsPageSectionDTO> response = new ApiResponse<>();
            response.setData(new AboutUsPageSectionDTO(pageSection, lang));
            return ResponseEntity.ok(response);
        }
        ApiResponse<AboutUsPageSection> response = new ApiResponse<>();
        response.setData(pageSection);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<AboutUsPageSection>> update(AboutUsPageSection pageSection) {
        ApiResponse<AboutUsPageSection> response = new ApiResponse<>();
        AboutUsPageSection dbSection = aboutUsPageSectionRepository.findById(pageSection.getId())
                .orElseThrow(() -> new NotFoundException("PageSection is not found by id: " + pageSection.getId()));

        if (pageSection.getTitleUz() != null) {
            dbSection.setTitleUz(pageSection.getTitleUz());
        }
        if (pageSection.getTitleRu() != null) {
            dbSection.setTitleRu(pageSection.getTitleRu());
        }
        if (pageSection.getTitleEn() != null) {
            dbSection.setTitleEn(pageSection.getTitleEn());
        }

        if (pageSection.getOptions() != null && !pageSection.getOptions().isEmpty()) {
            List<AboutUsSectionOption> sectionOptions = pageSection.getOptions();
            List<AboutUsSectionOption> dbOptions = dbSection.getOptions();

            for (AboutUsSectionOption sectionOption : sectionOptions) {
                for (AboutUsSectionOption dbOption : dbOptions) {
                    if (dbOption.getId().equals(sectionOption.getId())) {

                        if (sectionOption.getTextUz() != null) {
                            dbOption.setTextUz(sectionOption.getTextUz());
                        }
                        if (sectionOption.getTextRu() != null) {
                            dbOption.setTextRu(sectionOption.getTextRu());
                        }
                        if (sectionOption.getTextEn() != null) {
                            dbOption.setTextEn(sectionOption.getTextEn());
                        }

                    }
                }
            }
        }

        response.setData(aboutUsPageSectionRepository.save(dbSection));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        AboutUsPageSection pageSection = aboutUsPageSectionRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new NotFoundException("PageSection is not found"));
        aboutUsPageSectionRepository.delete(pageSection);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }

}
