package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.AboutUsHeaderOption;
import uz.imed.entity.AboutUsPageHeader;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.AboutUsPageHeaderDTO;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.AboutUsHeaderOptionRepository;
import uz.imed.repository.AboutUsPageHeaderRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsPageHeaderService {

    private final AboutUsPageHeaderRepository aboutUsPageHeaderRepository;

//    private final AboutUsHeaderOptionRepository aboutUsHeaderOptionRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<AboutUsPageHeader>> create(String json, MultipartFile photoFile) {
        ApiResponse<AboutUsPageHeader> response = new ApiResponse<>();
        Optional<AboutUsPageHeader> firstHeader = aboutUsPageHeaderRepository.findAll().stream().findFirst();
        try {
            AboutUsPageHeader header = objectMapper.readValue(json, AboutUsPageHeader.class);
            if (firstHeader.isPresent()) {
                header.setId(firstHeader.get().getId());
                return update(header);
            }
            header.setPhoto(photoService.save(photoFile));
            AboutUsPageHeader save = aboutUsPageHeaderRepository.save(header);
            response.setData(save);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> find(String lang) {
        AboutUsPageHeader header = aboutUsPageHeaderRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Header is not found"));
        if (lang != null) {
            ApiResponse<AboutUsPageHeaderDTO> response = new ApiResponse<>();
            response.setData(new AboutUsPageHeaderDTO(header, lang));
            return ResponseEntity.ok(response);
        }
        ApiResponse<AboutUsPageHeader> response = new ApiResponse<>();
        response.setData(header);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<AboutUsPageHeader>> update(AboutUsPageHeader header) {
        ApiResponse<AboutUsPageHeader> response = new ApiResponse<>();
        AboutUsPageHeader fromDB = aboutUsPageHeaderRepository.findById(header.getId())
                .orElseThrow(() -> new NotFoundException("Header is not found by id: " + header.getId()));

        if (header.getTitleUz() != null) {
            fromDB.setTitleUz(header.getTitleUz());
        }
        if (header.getTitleRu() != null) {
            fromDB.setTitleRu(header.getTitleRu());
        }
        if (header.getTitleEn() != null) {
            fromDB.setTitleEn(header.getTitleEn());
        }

        if (header.getSubTitleUz() != null) {
            fromDB.setSubTitleUz(header.getSubTitleUz());
        }
        if (header.getSubTitleRu() != null) {
            fromDB.setSubTitleRu(header.getSubTitleRu());
        }
        if (header.getSubTitleEn() != null) {
            fromDB.setSubTitleEn(header.getSubTitleEn());
        }

        if (header.getTextUz() != null) {
            fromDB.setTextUz(header.getTextUz());
        }
        if (header.getTextEn() != null) {
            fromDB.setTextEn(header.getTextEn());
        }
        if (header.getTextRu() != null) {
            fromDB.setTextRu(header.getTextRu());
        }

        if (header.getOptions() != null && !header.getOptions().isEmpty()) {
            List<AboutUsHeaderOption> dbOptions = fromDB.getOptions();
            List<AboutUsHeaderOption> headerOptions = header.getOptions();
            for (AboutUsHeaderOption headerOption : headerOptions) {
                for (AboutUsHeaderOption dbOption : dbOptions) {
                    if (dbOption.getId().equals(headerOption.getId())) {

                        if (headerOption.getTitleUz() != null) {
                            dbOption.setTitleUz(headerOption.getTitleUz());
                        }
                        if (headerOption.getTitleRu() != null) {
                            dbOption.setTitleRu(headerOption.getTitleRu());
                        }
                        if (headerOption.getTitleEn() != null) {
                            dbOption.setTitleEn(headerOption.getTitleEn());
                        }

                        if (headerOption.getTextUz() != null) {
                            dbOption.setTextUz(headerOption.getTextUz());
                        }
                        if (headerOption.getTextRu() != null) {
                            dbOption.setTextRu(headerOption.getTextRu());
                        }
                        if (headerOption.getTextEn() != null) {
                            dbOption.setTextEn(headerOption.getTextEn());
                        }

                    }
                }
            }
        }
        response.setData(aboutUsPageHeaderRepository.save(fromDB));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        AboutUsPageHeader header = aboutUsPageHeaderRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Header is not found"));
        aboutUsPageHeaderRepository.delete(header);
        response.setMessage("Successfully deleted");
        return ResponseEntity.ok(response);
    }

}
