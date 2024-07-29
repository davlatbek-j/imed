package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Partner;
import uz.imed.entity.Photo;
import uz.imed.exeptions.LanguageNotSupportException;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerDTO {
    Long id;

    String title;

    String mainDescription;

    String description;

    PhotoDTO photoDTO;

    String slug;

    public PartnerDTO(Partner partner,String lang) {
        this.photoDTO = new PhotoDTO(partner.getPhoto());
        this.slug = partner.getSlug();
        switch (lang.toLowerCase())
        {
            case "uz":
            {
                this.title = partner.getTitleUz();
                this.mainDescription = partner.getMainDescriptionUz();
                this.description=partner.getDescriptionUz();
                break;
            }
            case "ru":
            {
                this.title = partner.getTitleRu();
                this.mainDescription = partner.getMainDescriptionRu();
                this.description =partner.getDescriptionRu();
                break;
            }
            case "eng":
            {
                this.title = partner.getTitleEng();
                this.mainDescription = partner.getMainDescriptionEng();
                this.description =partner.getDescriptionEng();
                break;
            }
            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }

}
