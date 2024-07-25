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

    public ResponseEntity<ApiResponse<AboutUsHeader>> create(AboutUsHeader entity, MultipartFile photoFile) {
        ApiResponse<AboutUsHeader> response = new ApiResponse<>();
       AboutUsHeader aboutUsHeader=new AboutUsHeader();
       aboutUsHeader.setPhoto(photoService.save(photoFile));
       aboutUsHeader.setFormName(entity.getFormName());
       aboutUsHeader.setTitleUz(entity.getTitleUz());
       aboutUsHeader.setTitleRu(entity.getTitleRu());
       aboutUsHeader.setTitleEng(entity.getTitleEng());
       aboutUsHeader.setSubtitleUz(entity.getSubtitleUz());
       aboutUsHeader.setSubtitleRu(entity.getSubtitleRu());
       aboutUsHeader.setSubtitleEng(entity.getSubtitleEng());
       aboutUsHeader.setDescriptionUz(entity.getDescriptionUz());
       aboutUsHeader.setDescriptionRu(entity.getDescriptionRu());
       aboutUsHeader.setDescriptionEng(entity.getDescriptionEng());

        AboutUsHeader saved = aboutUsHeaderRepository.save(aboutUsHeader);
        response.setMessage("Successfully created");
        response.setData(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    public ResponseEntity<ApiResponse<AboutUsHeaderDTO>> getById(Long id, String lang) {
        ApiResponse<AboutUsHeaderDTO> response = new ApiResponse<>();
        if (!aboutUsHeaderRepository.existsById(id)) {
            response.setMessage("Article with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        AboutUsHeader aboutUsHeader = aboutUsHeaderRepository.findById(id).get();
        response.setMessage("Found article with id " + id);

        response.setData(new AboutUsHeaderDTO(aboutUsHeader, lang));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<List<AboutUsHeaderDTO>>> findAll(String lang) {
        ApiResponse<List<AboutUsHeaderDTO>> response = new ApiResponse<>();
        List<AboutUsHeader> articles = aboutUsHeaderRepository.findAll();
        response.setMessage("Found " + articles.size() + " article(s)");
        response.setData(new ArrayList<>());
        articles.forEach(i -> response.getData().add(new AboutUsHeaderDTO(i, lang)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<ApiResponse<AboutUsHeader>> update(Long id ,AboutUsHeader newAboutUsHeader,MultipartFile photo) {
        ApiResponse<AboutUsHeader> response = new ApiResponse<>();
        Optional<AboutUsHeader> optionalAboutUsHeader = aboutUsHeaderRepository.findAll().stream().findFirst();
        if (optionalAboutUsHeader.isEmpty()) {
            response.setMessage("AboutUsHeader is not found");
            return ResponseEntity.status(404).body(response);
        }
        AboutUsHeader newentity = aboutUsHeaderRepository.findById(id).get();

        if (newAboutUsHeader.getTitleUz() != null || !newAboutUsHeader.getTitleUz().isEmpty()) {
            newentity.setTitleUz(newAboutUsHeader.getTitleUz());
        }
        if (newAboutUsHeader.getTitleRu() != null || !newAboutUsHeader.getTitleRu().isEmpty()) {
            newentity.setTitleRu(newAboutUsHeader.getTitleRu());
        }
        if (newAboutUsHeader.getTitleEng() != null || !newAboutUsHeader.getTitleEng().isEmpty()) {
            newentity.setTitleUz(newAboutUsHeader.getTitleUz());
        }
        if (newAboutUsHeader.getFormName() != null || !newAboutUsHeader.getFormName().isEmpty()) {
            newentity.setFormName(newAboutUsHeader.getFormName());
        }
        if (newAboutUsHeader.getSubtitleUz() != null || !newAboutUsHeader.getSubtitleUz().isEmpty()) {
            newentity.setSubtitleUz(newAboutUsHeader.getSubtitleUz());
        }
        if (newAboutUsHeader.getSubtitleRu() != null || !newAboutUsHeader.getSubtitleRu().isEmpty()) {
            newentity.setSubtitleRu(newAboutUsHeader.getSubtitleRu());
        }
        if (newAboutUsHeader.getSubtitleEng() != null || !newAboutUsHeader.getSubtitleEng().isEmpty()) {
            newentity.setSubtitleEng(newAboutUsHeader.getSubtitleEng());
        }

        if (newAboutUsHeader.getDescriptionUz() != null || !newAboutUsHeader.getDescriptionUz().isEmpty()) {
            newentity.setDescriptionUz(newAboutUsHeader.getDescriptionUz());
        }
        if (newAboutUsHeader.getDescriptionRu() != null || !newAboutUsHeader.getDescriptionRu().isEmpty()) {
            newentity.setDescriptionRu(newAboutUsHeader.getDescriptionRu());
        }
        if (newAboutUsHeader.getDescriptionEng() != null || !newAboutUsHeader.getDescriptionEng().isEmpty()) {
            newentity.setDescriptionEng(newAboutUsHeader.getDescriptionEng());
        }
        if (photo!=null||!photo.isEmpty()){
            newentity.setPhoto(photoService.save(photo));
        }

        newentity.setId(id);
        AboutUsHeader save = aboutUsHeaderRepository.save(newentity);

        response.setMessage("Successfully updated");
        response.setData(save);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
