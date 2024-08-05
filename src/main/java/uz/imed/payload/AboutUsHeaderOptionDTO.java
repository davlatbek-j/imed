package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.AboutUsHeaderOption;
import uz.imed.exception.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutUsHeaderOptionDTO {

    Long id;

    String title;

    String text;

    public AboutUsHeaderOptionDTO(AboutUsHeaderOption option, String lang) {
        this.id = option.getId();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.title = option.getTitleUz();
                this.text = option.getTextUz();
                break;
            }

            case "ru": {
                this.title = option.getTitleRu();
                this.text = option.getTextRu();
                break;
            }

            case "en": {
                this.title = option.getTitleEn();
                this.text = option.getTextEn();
                break;
            }

            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);

        }
    }

}
