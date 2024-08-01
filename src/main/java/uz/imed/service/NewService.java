package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.imed.entity.New;
import uz.imed.entity.translation.NewTranslation;
import uz.imed.exeptions.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.NewDTO;
import uz.imed.repository.NewRepository;
import uz.imed.repository.NewTranslationRepository;
import uz.imed.util.SlugUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NewService
{

    private final NewRepository newRepository;

    private final NewTranslationRepository newTranslationRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<New>> create(String json, MultipartFile photoFile) {
        ApiResponse<New> response = new ApiResponse<>();
        try {
            New aNew = objectMapper.readValue(json, New.class);
            aNew.setActive(true);
            aNew.setPhoto(photoService.save(photoFile));
            New savedNew = newRepository.save(aNew);
            for (NewTranslation translation : aNew.getTranslations()) {
                translation.setNewness(savedNew);
                newTranslationRepository.save(translation);
            }
            String slug = savedNew.getId() + "-" + SlugUtil.makeSlug(getClientNameForSlug(aNew.getTranslations()));
            newRepository.updateSlug(slug, savedNew.getId());
            savedNew.setSlug(slug);
            response.setData(savedNew);
            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    private String getClientNameForSlug(List<NewTranslation> translations) {
        return translations
                .stream()
                .filter(translation -> translation.getLanguage().equals("en"))
                .findFirst()
                .map(NewTranslation::getHeading)
                .orElse(null);
    }

    public ResponseEntity<ApiResponse<NewDTO>> findBySlug(String slug, String lang) {
        ApiResponse<NewDTO> response = new ApiResponse<>();
        New aNew = newRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("New is not found by slug: " + slug));
        response.setData(new NewDTO(aNew, lang));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<New>> findFullDataById(Long id) {
        ApiResponse<New> response = new ApiResponse<>();
        New aNew = newRepository.findById(id).orElseThrow(() -> new NotFoundException("New is not found by id: " + id));
        response.setData(aNew);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> findAll(String lang) {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<New> all = newRepository.findAll();
        all.forEach(aNew -> response.getData().add(new NewDTO(aNew, lang)));
        response.setMessage("Found " + all.size() + " aNew(s)");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<New>> update(New newClient) {
        ApiResponse<New> response = new ApiResponse<>();
        New existClient = newRepository.findById(newClient.getId()).orElseThrow(() -> new NotFoundException("New is not found by id: " + newClient.getId()));
        if (newClient.getTranslations() != null) {
            for (NewTranslation newTranslation : newClient.getTranslations()) {
                NewTranslation existTranslation = existClient.getTranslations()
                        .stream()
                        .filter(clientTranslation -> clientTranslation.getLanguage().equals(newTranslation.getLanguage()))
                        .findFirst()
                        .orElseThrow(null);
                if (existTranslation != null) {
                    if (newTranslation.getHeading() != null) {
                        if (newTranslation.getHeading().equals("en")) {
                            String slug = existClient.getId() + "-" + SlugUtil.makeSlug(newTranslation.getHeading());
                            existClient.setSlug(slug);
                        }
                        existTranslation.setHeading(newTranslation.getHeading());
                    }
                    if (newTranslation.getText() != null) {
                        existTranslation.setText(newTranslation.getText());
                    }

                    existTranslation.setNewness(existClient);
                }
            }
        }
        New save = newRepository.save(existClient);
        response.setData(save);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!newRepository.existsById(id)) {
            throw new NotFoundException("New is not found by id: " + id);
        }
        newRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }


}
