package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.AboutUsHeader;
import uz.imed.exeptions.LanguageNotSupportException;

import java.util.ArrayList;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutUsHeaderDTO {
    Long id;
    String formName;
    String title;
    String subtitle;
    String description;
    PhotoDTO photo;

    public AboutUsHeaderDTO(AboutUsHeader entity, String lang)
    {
        this.id = entity.getId();
        this.formName=entity.getFormName();
        this.photo = new PhotoDTO(entity.getPhoto());

        switch (lang.toLowerCase())
        {
            case "uz":
            {
                this.title = entity.getTitleUz();
                this.subtitle = entity.getSubtitleUz();
                this.description=entity.getDescriptionUz();
                break;
            }
            case "ru":
            {
                this.title = entity.getTitleRu();
                this.subtitle = entity.getSubtitleRu();
                this.description =entity.getDescriptionRu();
                break;
            }
            case "eng":
            {
                this.title = entity.getTitleEng();
                this.subtitle = entity.getSubtitleEng();
                this.description =entity.getDescriptionEng();
                break;
            }
            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }
}
