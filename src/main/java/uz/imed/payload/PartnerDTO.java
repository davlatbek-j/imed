package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Partner;
import uz.imed.entity.PartnerTranslation;
import uz.imed.entity.Photo;
import uz.imed.exception.LanguageNotSupportException;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PartnerDTO {

    Long id;

    String slug;

    Photo logo;

    Boolean active;

    String website;

    String name;

    String note;

    String about;

    Integer orderNum;

    public PartnerDTO(Partner partner, String lang) {
        this.id = partner.getId();
        this.slug = partner.getSlug();
        this.logo = partner.getLogo();
        this.active = partner.getActive();
        this.website = partner.getWebsite();
        PartnerTranslation partnerTranslation = getTranslationByLang(lang, partner.getTranslations());
        this.name = partnerTranslation.getName();
        this.note = partnerTranslation.getNote();
        this.about = partnerTranslation.getAbout();
    }

    private PartnerTranslation getTranslationByLang(String lang, List<PartnerTranslation> partnerTranslations) {
        return partnerTranslations
                .stream()
                .filter(translation -> translation.getLanguage().equals(lang))
                .findFirst()
                .orElseThrow(() -> new LanguageNotSupportException("Language not supported: " + lang));
    }

}
