package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.PartnerHeader;
import uz.imed.exeptions.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerHeaderDTO {

    Long id;

    String description;

    public PartnerHeaderDTO(PartnerHeader partnerHeader,String lang){
        this.id= partnerHeader.getId();
        switch (lang.toLowerCase())
        {
            case "uz":
            {

                this.description=partnerHeader.getDescriptionUz();
                break;
            }
            case "ru":
            {

                this.description =partnerHeader.getDescriptionRu();
                break;
            }
            case "eng":
            {
                this.description =partnerHeader.getDescriptionEng();
                break;
            }
            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }
}
