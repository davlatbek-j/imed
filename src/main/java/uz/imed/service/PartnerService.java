package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Partner;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.PartnerDTO;
import uz.imed.repository.PartnerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<Partner>> create(String strPartner, MultipartFile photoFile) {
        ApiResponse<Partner> response = new ApiResponse<>();
        try {
            Partner partner = objectMapper.readValue(strPartner, Partner.class);
            partner.setPhoto(photoService.save(photoFile));
            partner.setActive(true);
            Partner save = partnerRepository.save(partner);
            String slug = save.getId() + "-" + save.getTitle();
            partnerRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<Partner>> findById(Long id) {
        ApiResponse<Partner> response = new ApiResponse<>();
        Optional<Partner> optionalPartner = partnerRepository.findById(id);
        if (optionalPartner.isEmpty()) {
            response.setMessage("Partner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Partner partner = optionalPartner.get();
        response.setData(partner);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Partner>> findBySlug(String slug) {
        ApiResponse<Partner> response = new ApiResponse<>();
        Optional<Partner> optionalPartner = partnerRepository.findBySlug(slug);
        if (optionalPartner.isEmpty()) {
            response.setMessage("Partner is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        Partner partner = optionalPartner.get();
        response.setData(partner);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<Partner>>> findAll() {
        ApiResponse<List<Partner>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAll();
        all.forEach(partner -> response.getData().add(partner));
        response.setMessage("Found " + all.size() + " partner(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<PartnerDTO>>> findSixPartnerForMainPage() {
        ApiResponse<List<PartnerDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAllByOrderByIdAsc();
        all.stream()
                .filter(Partner::isActive)
                .limit(6).toList()
                .forEach(partner -> response.getData().add(new PartnerDTO(partner)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<Partner>>> findOtherPartnerBySlug(String partnerSlug) {
        ApiResponse<List<Partner>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAllByOrderByIdAsc();
        all.stream()
                .filter(partner -> partner.isActive() && !partner.getSlug().equals(partnerSlug)).toList()
                .forEach(partner -> response.getData().add(partner));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (partnerRepository.findById(id).isEmpty()) {
            response.setMessage("Partner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        partnerRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Partner> optionalPartner = partnerRepository.findById(id);
        if (optionalPartner.isEmpty()) {
            response.setMessage("Partner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Partner partner = optionalPartner.get();
        boolean active = !partner.isActive();
        partnerRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current partner active: " + active);
        return ResponseEntity.status(200).body(response);
    }


}
