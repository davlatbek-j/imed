package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.EventAbout;
import uz.imed.exception.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventAboutDTO {

    Long id;

    String title;

    String text;

    public EventAboutDTO(EventAbout eventAbout, String lang) {
        this.id = eventAbout.getId();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.title = eventAbout.getTitleUz();
                this.text = eventAbout.getTextUz();
                break;
            }

            case "ru": {
                this.title = eventAbout.getTitleRu();
                this.text = eventAbout.getTextRu();
                break;
            }

            case "en": {
                this.title = eventAbout.getTitleEn();
                this.text = eventAbout.getTextEn();
                break;
            }

            default:
                throw new LanguageNotSupportException("Language not supported: "+lang);

        }
    }

}
