package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.AboutUsChooseSection;
import uz.imed.exception.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutUsChooseSectionDTO {

    Long id;

    String title;

    String text;

    public AboutUsChooseSectionDTO(AboutUsChooseSection section, String lang) {
        this.id = section.getId();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.title = section.getTitleUz();
                this.text = section.getTextUz();
                break;
            }

            case "ru": {
                this.title = section.getTitleRu();
                this.text = section.getTextRu();
                break;
            }

            case "en": {
                this.title = section.getTitleEn();
                this.text = section.getTextEn();
                break;
            }

            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);

        }
    }

}
