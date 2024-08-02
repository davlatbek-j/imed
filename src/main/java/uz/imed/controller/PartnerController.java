package uz.imed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.imed.entity.Partner;
import uz.imed.entity.Photo;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

//    @Autowired
//    private PartnerService partnerService;

    @PostMapping
    public ResponseEntity<Partner> createPartner(@RequestBody PartnerRequest partnerRequest) {
        Partner partner = new Partner();
        partner.setSlug(partnerRequest.getSlug());
        partner.setLogo(partnerRequest.getLogo());
        partner.setActive(partnerRequest.isActive());

//        List<PartnerTranslation> translations = partnerRequest.getTranslations();

//        Partner createdPartner = partnerService.createPartner(partner, translations);
//        return ResponseEntity.ok(createdPartner);
        return null;
    }
}

class PartnerRequest {
    private String slug;
    private Photo logo;
    private boolean active;
//    private List<PartnerTranslation> translations;

    // Getters and setters

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Photo getLogo() {
        return logo;
    }

    public void setLogo(Photo logo) {
        this.logo = logo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

//    public List<PartnerTranslation> getTranslations() {
//        return translations;
//    }

//    public void setTranslations(List<PartnerTranslation> translations) {
//        this.translations = translations;
//    }
}
