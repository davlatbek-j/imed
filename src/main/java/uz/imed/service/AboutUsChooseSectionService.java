package uz.imed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.imed.entity.AboutUsChooseSection;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.AboutUsChooseSectionDTO;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.AboutUsChooseSectionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AboutUsChooseSectionService {

    private final AboutUsChooseSectionRepository chooseSectionRepository;

    public ResponseEntity<ApiResponse<AboutUsChooseSection>> create(AboutUsChooseSection chooseSection) {
        ApiResponse<AboutUsChooseSection> response = new ApiResponse<>();
        AboutUsChooseSection save = chooseSectionRepository.save(chooseSection);
        response.setData(save);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> findById(Long id, String lang) {
        AboutUsChooseSection chooseSection = chooseSectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ChooseSection is not found"));
        if (lang != null) {
            ApiResponse<AboutUsChooseSectionDTO> response = new ApiResponse<>();
            response.setData(new AboutUsChooseSectionDTO(chooseSection, lang));
            return ResponseEntity.ok(response);
        }
        ApiResponse<AboutUsChooseSection> response = new ApiResponse<>();
        response.setData(chooseSection);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> findAll(String lang) {
        List<AboutUsChooseSection> all = chooseSectionRepository.findAll();
        if (lang != null) {
            ApiResponse<List<AboutUsChooseSectionDTO>> response = new ApiResponse<>();
            response.setData(new ArrayList<>());
            all.forEach(chooseSection -> response.getData().add(new AboutUsChooseSectionDTO(chooseSection, lang)));
            response.setMessage("Found " + all.size() + " chooseSection(s)");
            return ResponseEntity.ok(response);
        }
        ApiResponse<List<AboutUsChooseSection>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        all.forEach(chooseSection -> response.getData().add(chooseSection));
        response.setMessage("Found " + all.size() + " chooseSection(s)");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<AboutUsChooseSection>> update(AboutUsChooseSection chooseSection) {
        ApiResponse<AboutUsChooseSection> response = new ApiResponse<>();
        AboutUsChooseSection fromDB = chooseSectionRepository.findById(chooseSection.getId())
                .orElseThrow(() -> new NotFoundException("ChooseSection is not found by id: " + chooseSection.getId()));

        if (chooseSection.getTitleUz() != null) {
            fromDB.setTitleUz(chooseSection.getTitleUz());
        }
        if (chooseSection.getTitleRu() != null) {
            fromDB.setTitleRu(chooseSection.getTitleRu());
        }
        if (chooseSection.getTitleEn() != null) {
            fromDB.setTitleEn(chooseSection.getTitleEn());
        }

        if (chooseSection.getTextUz() != null) {
            fromDB.setTextUz(chooseSection.getTextUz());
        }
        if (chooseSection.getTextRu() != null) {
            fromDB.setTextRu(chooseSection.getTextRu());
        }
        if (chooseSection.getTextEn() != null) {
            fromDB.setTextEn(chooseSection.getTextEn());
        }

        response.setData(chooseSectionRepository.save(fromDB));
        return ResponseEntity.ok(response);

    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!chooseSectionRepository.existsById(id)) {
            throw new NotFoundException("ChooseSection is not found by id: " + id);
        }
        chooseSectionRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }

}
