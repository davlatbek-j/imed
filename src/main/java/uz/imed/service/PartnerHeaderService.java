package uz.imed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.imed.entity.PartnerHeader;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.PartnerHeaderRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerHeaderService {

    private final PartnerHeaderRepository headerRepository;

    public ResponseEntity<ApiResponse<PartnerHeader>> create(PartnerHeader partnerHeader) {
        ApiResponse<PartnerHeader> response = new ApiResponse<>();
        Optional<PartnerHeader> optional = headerRepository.findAll().stream().findFirst();
        if (optional.isPresent()) {
            return update(partnerHeader);
        }
        PartnerHeader save = headerRepository.save(partnerHeader);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<PartnerHeader>> find() {
        ApiResponse<PartnerHeader> response = new ApiResponse<>();
        Optional<PartnerHeader> optional = headerRepository.findAll().stream().findFirst();
        if (optional.isEmpty()) {
            response.setMessage("PartnerHeader is not found");
            return ResponseEntity.status(404).body(response);
        }
        PartnerHeader partnerHeader = optional.get();
        response.setData(partnerHeader);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<PartnerHeader>> update(PartnerHeader partnerHeader) {
        ApiResponse<PartnerHeader> response = new ApiResponse<>();
        Optional<PartnerHeader> optional = headerRepository.findAll().stream().findFirst();
        if (optional.isEmpty()) {
            response.setMessage("PartnerHeader is not found");
            return ResponseEntity.status(404).body(response);
        }
        PartnerHeader oldHeader = optional.get();
        oldHeader.setDescription(partnerHeader.getDescription());
        PartnerHeader save = headerRepository.save(oldHeader);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<PartnerHeader> optional = headerRepository.findAll().stream().findFirst();
        if (optional.isEmpty()) {
            response.setMessage("Header is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optional.get().getId();
        headerRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

}
