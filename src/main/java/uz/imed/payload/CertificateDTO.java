package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Certificate;
import uz.imed.exeptions.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateDTO {

    Long id;

    String title;

    String description;

    PhotoDTO photoDTO;

    public CertificateDTO(Certificate certificate,String lang){
        this.id= certificate.getId();;
        this.photoDTO=new PhotoDTO(certificate.getCertificateImage());

        switch (lang.toLowerCase()){
            case "uz":
            {
                this.title=certificate.getTitleUz();
                this.description=certificate.getDescriptionUz();
                break;
            }
            case "ru":
            {
                this.title=certificate.getTitleRu();
                this.description=certificate.getDescriptionRu();
                break;
            }
            case "eng":
            {
                this.title=certificate.getTitleEng();
                this.description=certificate.getDescriptionEng();
                break;
            }
            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);

        }
    }
}
