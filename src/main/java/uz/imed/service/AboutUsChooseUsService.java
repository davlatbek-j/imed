package uz.imed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.imed.entity.AboutUsChooseUs;
import uz.imed.payload.AboutUsChooseUsDTO;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.AboutUsChooseUsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsChooseUsService {


    private final AboutUsChooseUsRepository aboutUsChooseUsRepository;

    public ResponseEntity<ApiResponse<AboutUsChooseUs>> create(AboutUsChooseUs aboutUsChooseUs) {
        ApiResponse<AboutUsChooseUs> response = new ApiResponse<>();
        AboutUsChooseUs article = new AboutUsChooseUs();
        article.setNameUz(aboutUsChooseUs.getNameUz());
        article.setNameRu(aboutUsChooseUs.getNameRu());
        article.setNameEng(aboutUsChooseUs.getNameEng());
        article.setDescriptionUz(aboutUsChooseUs.getDescriptionUz());
        article.setDescriptionRu(aboutUsChooseUs.getDescriptionRu());
        article.setDescriptionEng(aboutUsChooseUs.getDescriptionEng());


        AboutUsChooseUs saved = aboutUsChooseUsRepository.save(article);
        response.setMessage("Successfully created");
        response.setData(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse<AboutUsChooseUsDTO>> getById(Long id, String lang) {
        ApiResponse<AboutUsChooseUsDTO> response = new ApiResponse<>();
        if (!aboutUsChooseUsRepository.existsById(id)) {
            response.setMessage("Article with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        AboutUsChooseUs aboutUsChooseUs = aboutUsChooseUsRepository.findById(id).get();
        response.setMessage("Found article with id " + id);

        response.setData(new AboutUsChooseUsDTO(aboutUsChooseUs, lang));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<List<AboutUsChooseUsDTO>>> findAll(String lang) {
        ApiResponse<List<AboutUsChooseUsDTO>> response = new ApiResponse<>();
        List<AboutUsChooseUs> all = aboutUsChooseUsRepository.findAll();
        response.setMessage("Found " + all.size() + " data");
        response.setData(new ArrayList<>());
        all.forEach(i -> response.getData().add(new AboutUsChooseUsDTO(i, lang)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<AboutUsChooseUs>> update(Long id, AboutUsChooseUs entity) {
        ApiResponse<AboutUsChooseUs> response = new ApiResponse<>();
        if (!aboutUsChooseUsRepository.existsById(id)) {
            response.setMessage("Data with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        AboutUsChooseUs newEntity = aboutUsChooseUsRepository.findById(id).get();

        if (entity.getNameUz() != null ) {
            newEntity.setNameUz(entity.getNameUz());
        }
        if (entity.getNameRu() != null ) {
            newEntity.setNameRu(entity.getNameRu());
        }
        if (entity.getNameEng() != null ) {
            newEntity.setNameEng(entity.getNameEng());
        }
        if (entity.getDescriptionUz() != null ) {
            newEntity.setDescriptionUz(entity.getDescriptionUz());
        }
        if (entity.getDescriptionRu() != null ) {
            newEntity.setDescriptionRu(entity.getDescriptionRu());
        }


        newEntity.setId(id);
        AboutUsChooseUs save = aboutUsChooseUsRepository.save(newEntity);

        response.setMessage("Successfully updated");
        response.setData(save);
        return new ResponseEntity<>(response, HttpStatus.OK);

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
