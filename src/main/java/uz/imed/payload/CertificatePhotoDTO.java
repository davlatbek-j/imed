package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Certificate;
import uz.imed.entity.Photo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificatePhotoDTO {

    Long id;

    String slug;

    Photo photo;

    Boolean active;

    public CertificatePhotoDTO(Certificate certificate) {
        this.id = certificate.getId();
        this.slug = certificate.getSlug();
        this.photo = certificate.getPhoto();
        this.active = certificate.getActive();
    }

}
