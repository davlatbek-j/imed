package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.AboutUsHeader;
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

    public ResponseEntity<ApiResponse<Partner>> create(Partner partner1) {
        ApiResponse<Partner> response = new ApiResponse<>();

            Partner partner = new Partner();

            partner.setActive(true);
            partner.setTitleUz(partner1.getTitleUz());
            partner.setTitleRu(partner1.getTitleRu());
            partner.setTitleEng(partner1.getTitleEng());
            partner.setMainDescriptionUz(partner1.getMainDescriptionUz());
            partner.setMainDescriptionRu(partner1.getMainDescriptionRu());
            partner.setMainDescriptionEng(partner1.getMainDescriptionEng());
            partner.setDescriptionUz(partner1.getDescriptionUz());
            partner.setDescriptionRu(partner1.getDescriptionRu());
            partner.setDescriptionEng(partner1.getDescriptionEng());
            Partner save = partnerRepository.save(partner);
            String slug = save.getId() + "-" + save.getTitleEng();
            partnerRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            response.setMessage("Partner succesfully created");
            return ResponseEntity.status(201).body(response);

    }

    public ResponseEntity<ApiResponse<Partner>> uploadImage(Long id, MultipartFile file)
    {
        ApiResponse<Partner> response = new ApiResponse<>();

        if (!(file.getContentType().equals("image/png") ||
                file.getContentType().equals("image/svg+xml"))) {
            response.setMessage("Invalid file , only image/png or image/svg+xml");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!partnerRepository.existsById(id)) {
            response.setMessage("Data with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Partner partner = partnerRepository.findById(id).get();
        response.setMessage("Found data with id " + id);
        partner.setPhoto(photoService.save(file));
        Partner save = partnerRepository.save(partner);
        response.setMessage("Successfully created");
        response.setData(save);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    public ResponseEntity<ApiResponse<List<PartnerDTO>>> findAll(String lang) {
        ApiResponse<List<PartnerDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAll();
        all.forEach(partner -> response.getData().add(new PartnerDTO(partner,lang)));
        response.setMessage("Found " + all.size() + " partner(s)");
        return ResponseEntity.status(200).body(response);
    }

   public ResponseEntity<ApiResponse<List<PartnerDTO>>> findSixPartnerForMainPage(String lang) {
        ApiResponse<List<PartnerDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAllByOrderByIdAsc();
        all.stream()
                .filter(Partner::isActive)
                .limit(6).toList()
                .forEach(partner -> response.getData().add(new PartnerDTO(partner,lang)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<PartnerDTO>>> findOtherPartnerBySlug(String partnerSlug,String lang) {
        ApiResponse<List<PartnerDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAllByOrderByIdAsc();
        all.stream()
                .filter(partner -> partner.isActive() && !partner.getSlug().equals(partnerSlug)).toList()
                .forEach(partner -> response.getData().add(new PartnerDTO(partner,lang)));
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
