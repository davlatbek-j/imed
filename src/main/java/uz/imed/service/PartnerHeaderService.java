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
        response.setData(saved);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> get(String lang) {

        if (lang != null) {
            ApiResponse<PartnerHeaderDTO> response = new ApiResponse<>();
            PartnerHeader header = partnerHeaderRepository.findAll()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Header is null, or not created yet"));
            response.setData(new PartnerHeaderDTO(header, lang));
            response.setMessage("Language: " + lang);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<PartnerHeader> response = new ApiResponse<>();
            PartnerHeader header = partnerHeaderRepository.findAll()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Header is null, or not created yet"));
            response.setData(header);
            response.setMessage("All language");
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<ApiResponse<PartnerHeader>> update(PartnerHeader newHeader) {
        ApiResponse<PartnerHeader> response = new ApiResponse<>();
        PartnerHeader existHeader = partnerHeaderRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Header is null, or not created yet"));

        if (newHeader.getTitleUz() != null) {
            existHeader.setTitleUz(newHeader.getTitleUz());
        }
        if (newHeader.getTitleRu() != null) {
            existHeader.setTitleRu(newHeader.getTitleRu());
        }
        if (newHeader.getTitleEn() != null) {
            existHeader.setTitleEn(newHeader.getTitleEn());
        }

        if (newHeader.getTextUz() != null) {
            existHeader.setTextUz(newHeader.getTextUz());
        }
        if (newHeader.getTextRu() != null) {
            existHeader.setTextRu(newHeader.getTextRu());
        }
        if (newHeader.getTextEn() != null) {
            existHeader.setTextEn(newHeader.getTextEn());
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
