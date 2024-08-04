package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Partner;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.PartnerDTO;
import uz.imed.payload.PartnerLogoNameDTO;
import uz.imed.repository.PartnerRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;
    
    public ResponseEntity<ApiResponse<Partner>> create(String json, MultipartFile logo) {
        ApiResponse<Partner> response = new ApiResponse<>();
        try {
            Partner partner = objectMapper.readValue(json, Partner.class);
            //for getting id save empty entity and use this id, id uses for making slug.
            partner.setId(partnerRepository.save(new Partner()).getId());
            partner.setSlug(partner.getId() + "-" + SlugUtil.makeSlug(partner.getName()));
            partner.setLogo(photoService.save(logo));

            response.setData(partnerRepository.save(partner));
            response.setMessage("Added");
            return ResponseEntity.ok().body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> get(String slug, String lang) {
        Partner partner = partnerRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Partner is not found by slug: " + slug));
        if (lang == null) {
            ApiResponse<Partner> response = new ApiResponse<>();
            response.setData(partner);
            response.setMessage("All languages");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<PartnerDTO> response = new ApiResponse<>();
            response.setData(new PartnerDTO(partner, lang));
            response.setMessage("Language: " + lang);
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> all(String lang, boolean onlyLogoName) {

        List<Partner> partners = partnerRepository.findAll();
        if (lang == null) {
            ApiResponse<List<Partner>> response = new ApiResponse<>();
            response.setData(partners);
            response.setMessage("Found " + partners.size() + " partner(s)");
            return ResponseEntity.ok(response);
        } else if (onlyLogoName) {
            ApiResponse<List<PartnerLogoNameDTO>> response = new ApiResponse<>();
            response.setData(new ArrayList<>());
            partners.forEach(i -> response.getData().add(new PartnerLogoNameDTO(i)));
            response.setMessage("Found " + partners.size() + " partner(s)");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<PartnerDTO>> response = new ApiResponse<>();
            response.setData(new ArrayList<>());
            partners.forEach(i -> response.getData().add(new PartnerDTO(i, lang)));
            response.setMessage("Found " + partners.size() + " partner(s)");
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<ApiResponse<Partner>> update(Partner newPartner) {
        ApiResponse<Partner> response = new ApiResponse<>();
        if (newPartner == null || newPartner.getId() == null) {
            throw new RuntimeException("Partner or id not given");
        }

        Partner fromDB = partnerRepository.findById(newPartner.getId()).orElseThrow(() -> new NotFoundException("Partner is not found by id: " + newPartner.getId()));

        if (newPartner.getName() != null) {
            fromDB.setName(newPartner.getName());
            fromDB.setSlug(fromDB.getId() + "-" + newPartner.getName());
        }

        if (newPartner.getNoteUz() != null)
            fromDB.setNoteUz(newPartner.getNoteUz());

        if (newPartner.getNoteRu() != null)
            fromDB.setNoteRu(newPartner.getNoteRu());

        if (newPartner.getNoteEn() != null)
            fromDB.setNoteEn(newPartner.getNoteEn());

        if (newPartner.getAboutUz() != null)
            fromDB.setAboutUz(newPartner.getAboutUz());

        if (newPartner.getAboutRu() != null)
            fromDB.setAboutRu(newPartner.getAboutRu());

        if (newPartner.getAboutEn() != null)
            fromDB.setAboutEn(newPartner.getAboutEn());

        if (newPartner.getWebsite() != null)
            fromDB.setWebsite(newPartner.getWebsite());

        if (newPartner.getActive() != null)
            fromDB.setActive(newPartner.getActive());

        response.setData(partnerRepository.save(fromDB));
        response.setMessage("Updated");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        if (!partnerRepository.existsById(id)) {
            throw new NotFoundException("Partner is not found by id: " + id);
        }
        ApiResponse<Partner> response = new ApiResponse<>();
        partnerRepository.deleteById(id);
        response.setMessage("Deleted");
        return ResponseEntity.ok(response);
    }
}
