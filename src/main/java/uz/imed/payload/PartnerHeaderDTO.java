package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.PartnerHeader;
import uz.imed.entity.PartnerHeaderTranslation;
import uz.imed.exception.LanguageNotSupportException;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerHeaderDTO {

    Long id;

    String name;

    String description;

    public PartnerHeaderDTO(PartnerHeader partnerHeader, String lang) {
        this.id = partnerHeader.getId();
        PartnerHeaderTranslation translation = TranslationHelper.getTranslationByLang(lang, partnerHeader.getTranslations());
        this.name = translation.getName();
        this.description = translation.getDescription();
    }

    static class TranslationHelper {
        static PartnerHeaderTranslation getTranslationByLang(String lang, List<PartnerHeaderTranslation> translations) {
            return translations
                    .stream()
                    .filter(translation -> translation.getLanguage().equals(lang))
                    .findFirst()
                    .orElseThrow(() -> new LanguageNotSupportException("Language not supported: " + lang));
        }
    }

}
