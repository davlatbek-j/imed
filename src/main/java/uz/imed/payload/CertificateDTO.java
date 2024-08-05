package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Certificate;
import uz.imed.entity.Photo;
import uz.imed.exception.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateDTO {

    Long id;

    String slug;

    String title;

    String text;

    Photo photo;

    Boolean active;

    public CertificateDTO(Certificate certificate, String lang) {
        this.id = certificate.getId();
        this.slug = certificate.getSlug();
        this.photo = certificate.getPhoto();
        this.active = certificate.getActive();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.title = certificate.getTitleUz();
                this.text = certificate.getTextUz();
                break;
            }

            case "ru": {
                this.title = certificate.getTextRu();
                this.text = certificate.getTextRu();
                break;
            }

            case "en": {
                this.title = certificate.getTextEn();
                this.text = certificate.getTextEn();
                break;
            }

            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);

        }

    }

}
