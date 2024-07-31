package uz.imed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.imed.entity.PartnerHeader;
import uz.imed.entity.PartnerHeaderTranslation;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.PartnerHeaderDTO;
import uz.imed.repository.PartnerHeaderRepository;
import uz.imed.repository.PartnerHeaderTranslationRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerHeaderService {

    private final PartnerHeaderRepository partnerHeaderRepository;

    private final PartnerHeaderTranslationRepository partnerHeaderTranslationRepository;

    public ResponseEntity<ApiResponse<PartnerHeader>> create(PartnerHeader partnerHeader) {
        ApiResponse<PartnerHeader> response = new ApiResponse<>();
        Optional<PartnerHeader> firstHeaderOptional = partnerHeaderRepository.findAll().stream().findFirst();
        if (firstHeaderOptional.isPresent()) {
            return update(partnerHeader);
        }
        PartnerHeader saved = partnerHeaderRepository.save(partnerHeader);
        for (PartnerHeaderTranslation translation : partnerHeader.getTranslations()) {
            translation.setPartnerHeader(saved);
            partnerHeaderTranslationRepository.save(translation);
        }
        response.setData(saved);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<PartnerHeaderDTO>> find(String lang) {
        ApiResponse<PartnerHeaderDTO> response = new ApiResponse<>();
        PartnerHeader header = partnerHeaderRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Header not found"));
        response.setData(new PartnerHeaderDTO(header, lang));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<PartnerHeader>> findFullData() {
        ApiResponse<PartnerHeader> response = new ApiResponse<>();
        PartnerHeader header = partnerHeaderRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Header not found"));
        response.setData(header);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<PartnerHeader>> update(PartnerHeader newHeader) {
        ApiResponse<PartnerHeader> response = new ApiResponse<>();
        PartnerHeader existHeader = partnerHeaderRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Header is not found"));

        if (newHeader.getTranslations() != null) {
            for (PartnerHeaderTranslation newTranslation : newHeader.getTranslations()) {
                PartnerHeaderTranslation existTranslation = existHeader.getTranslations()
                        .stream()
                        .filter(translation -> translation.getLanguage().equals(newTranslation.getLanguage()))
                        .findFirst()
                        .orElseThrow(null);
                if (existTranslation != null) {
                    if (newTranslation.getName() != null) {
                        existTranslation.setName(newTranslation.getName());
                    }
                    if (newTranslation.getDescription() != null) {
                        existTranslation.setDescription(newTranslation.getDescription());
                    }
                    existTranslation.setPartnerHeader(existHeader);
                }
            }
        }
        PartnerHeader saved = partnerHeaderRepository.save(existHeader);
        response.setData(saved);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        PartnerHeader header = partnerHeaderRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Header not found"));
        partnerHeaderRepository.delete(header);
        response.setMessage("Successfully deleted");
        return ResponseEntity.ok(response);
    }

}
