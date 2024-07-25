package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.AboutUsChooseUs;
import uz.imed.exeptions.LanguageNotSupportException;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class AboutUsChooseUsDTO {

    Long id;

    String name;

    String description;

    public AboutUsChooseUsDTO(AboutUsChooseUs entity, String lang)
    {
        this.id = entity.getId();

        switch (lang.toLowerCase())
        {
            case "uz":
            {
                this.name = entity.getNameUz();
                this.description = entity.getDescriptionUz();
                break;
            }
            case "ru":
            {
                this.name = entity.getNameRu();
                this.description = entity.getDescriptionRu();
                break;
            }
            case "eng":{
                this.name = entity.getNameEng();
                this.description = entity.getDescriptionEng();
                break;
            }
            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }


}
