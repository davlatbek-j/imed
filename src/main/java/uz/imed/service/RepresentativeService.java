package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.ContactBody;
import uz.imed.entity.Representative;
import uz.imed.entity.translation.ContactBodyTranslation;
import uz.imed.entity.translation.RepresentativeTranslation;
import uz.imed.exeptions.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ContactBodyDTO;
import uz.imed.payload.RepresentativeDTO;
import uz.imed.repository.RepresentativeRepository;
import uz.imed.repository.RepresentativeTranslationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RepresentativeService {

    private final RepresentativeRepository representativeRepository;

    private final RepresentativeTranslationRepository repository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Representative>> create(String json, MultipartFile photoFile) {
        ApiResponse<Representative> response = new ApiResponse<>();
        try {
            Representative representative = objectMapper.readValue(json, Representative.class);
            representative.setActive(true);
            representative.setAddable(true);
            representative.setPhoto(photoService.save(photoFile));
            Representative save = representativeRepository.save(representative);
            for (RepresentativeTranslation translation : representative.getTranslations()) {
                translation.setRepresentative(save);
                repository.save(translation);
            }
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    public ResponseEntity<ApiResponse<RepresentativeDTO>> findById(String lang ,Long id) {
        ApiResponse<RepresentativeDTO> response = new ApiResponse<>();
        Optional<Representative> optional = representativeRepository.findById(id);
        if (optional.isEmpty()) {
            response.setMessage("Representative is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Representative representative = optional.get();
        response.setData(new RepresentativeDTO(representative,lang));
        return ResponseEntity.status(200).body(response);
    }


    public ResponseEntity<ApiResponse<List<RepresentativeDTO>>> findAll(String lang) {
        ApiResponse<List<RepresentativeDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Representative> all = representativeRepository.findAll();
        all.forEach(representative -> response.getData().add(new RepresentativeDTO(representative, lang)));
        response.setMessage("Found " + all.size() + " representative(s)");
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<Representative>> update(Representative newRepresentative) {
        ApiResponse<Representative> response = new ApiResponse<>();
        Representative existRepresentative = representativeRepository.findById(newRepresentative.getId()).orElseThrow(() -> new NotFoundException("representative is not found by id: " + newRepresentative.getId()));
        if (newRepresentative.getTranslations() != null) {
            for (RepresentativeTranslation newTranslation : newRepresentative.getTranslations()) {
                RepresentativeTranslation existTranslation = existRepresentative.getTranslations()
                        .stream()
                        .filter(clientTranslation -> clientTranslation.getLanguage().equals(newTranslation.getLanguage()))
                        .findFirst()
                        .orElseThrow(null);
                if (existTranslation != null) {
                    if (newTranslation.getName() != null) {
                        existTranslation.setName(newTranslation.getName());
                    }
                    if (newTranslation.getCountry() != null) {
                        existTranslation.setCountry(newTranslation.getCountry());
                    }
                    if (newTranslation.getAddress() != null) {
                        existTranslation.setAddress(newTranslation.getAddress());
                    }
                    if (newTranslation.getSchedule() != null) {
                        existTranslation.setSchedule(newTranslation.getSchedule());
                    }

                    existTranslation.setRepresentative(existRepresentative);
                }
            }
        }
        Representative save = representativeRepository.save(existRepresentative);
        response.setData(save);
        return ResponseEntity.ok(response);
    }



    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!representativeRepository.existsById(id)) {
            throw new NotFoundException("Representative is not found by id: " + id);
        }
        representativeRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Representative> optional = representativeRepository.findById(id);
        if (optional.isEmpty()) {
            response.setMessage("Representative is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        boolean active = !optional.get().isActive();
        representativeRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current representative active: " + active);
        return ResponseEntity.status(200).body(response);
    }

}
