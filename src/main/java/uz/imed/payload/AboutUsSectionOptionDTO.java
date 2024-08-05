package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.AboutUsSectionOption;
import uz.imed.exception.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutUsSectionOptionDTO {

    Long id;

    String text;

    public AboutUsSectionOptionDTO(AboutUsSectionOption sectionOption, String lang) {
        this.id = sectionOption.getId();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.text = sectionOption.getTextUz();
                break;
            }

            case "ru": {
                this.text = sectionOption.getTextRu();
                break;
            }

            case "en": {
                this.text = sectionOption.getTextEn();
                break;
            }

            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);

        }
    }

}
