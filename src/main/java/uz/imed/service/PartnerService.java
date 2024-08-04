package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Partner;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.PartnerDTO;
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

  /*  public ResponseEntity<ApiResponse<PartnerDTO>> findBySlug(String slug, String lang) {
        ApiResponse<PartnerDTO> response = new ApiResponse<>();
        Partner partner = partnerRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Partner is not found by slug: " + slug));
        response.setData(new PartnerDTO(partner, lang));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Partner>> findFullDataById(Long id) {
        ApiResponse<Partner> response = new ApiResponse<>();
        Partner partner = partnerRepository.findById(id).orElseThrow(() -> new NotFoundException("Partner is not found by id: " + id));
        response.setData(partner);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<PartnerDTO>>> findAll(String lang) {
        ApiResponse<List<PartnerDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAll();
        all.forEach(partner -> response.getData().add(new PartnerDTO(partner, lang)));
        response.setMessage("Found " + all.size() + " partner(s)");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Partner>> update(Partner newPartner) {
        ApiResponse<Partner> response = new ApiResponse<>();
        Partner existPartner = partnerRepository.findById(newPartner.getId()).orElseThrow(() -> new NotFoundException("Partner is not found by id: " + newPartner.getId()));

        if (newPartner.getWebsite() != null) {
            existPartner.setWebsite(newPartner.getWebsite());
        }
        if (newPartner.getActive() != null) {
            existPartner.setActive(newPartner.getActive());
        }
        if (newPartner.getTranslations() != null) {
            for (PartnerTranslation newTranslation : newPartner.getTranslations()) {
                PartnerTranslation existTranslation = existPartner.getTranslations()
                        .stream()
                        .filter(partnerTranslation -> partnerTranslation.getLanguage().equals(newTranslation.getLanguage()))
                        .findFirst()
                        .orElseThrow(null);
                if (existTranslation != null) {
                    if (newTranslation.getName() != null) {
                        if (newTranslation.getLanguage().equals("en")) {
                            String slug = existPartner.getId() + "-" + SlugUtil.makeSlug(newTranslation.getName());
                            existPartner.setSlug(slug);
                        }
                        existTranslation.setName(newTranslation.getName());
                    }
                    if (newTranslation.getNote() != null) {
                        existTranslation.setNote(newTranslation.getNote());
                    }
                    if (newTranslation.getAbout() != null) {
                        existTranslation.setAbout(newTranslation.getAbout());
                    }
                    existTranslation.setPartner(existPartner);
                }
            }
        }

        Partner save = partnerRepository.save(existPartner);
        response.setData(save);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!partnerRepository.existsById(id)) {
            throw new NotFoundException("Partner is not found by id: " + id);
        }
        partnerRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }
*/
    //TODO -----------------------
    public ResponseEntity<ApiResponse<?>> get(String slug, String lang) {
        Partner partner = partnerRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Partner is not found by slug: " + slug));
        if (lang == null) {
            ApiResponse<Partner> response = new ApiResponse<>();
            response.setData(partner);
            response.setMessage("All languages");
            return ResponseEntity.ok(response);
        }else
        {
            ApiResponse<PartnerDTO> response = new ApiResponse<>();
            return null;

        }
    }
}
