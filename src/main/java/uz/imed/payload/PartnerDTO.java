package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Partner;
import uz.imed.entity.Photo;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerDTO {

    Photo photo;

    String slug;

    public PartnerDTO(Partner partner) {
        this.photo = partner.getPhoto();
        this.slug = partner.getSlug();
    }

}
