package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Partner;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PartnerDTO {

    Long id;

    String slug;

    PhotoDTO logo;

    Boolean active;

    String website;

    String name;

    String note;

    String about;

    Integer orderNum;

    public PartnerDTO(Partner partner, String lang) {
        this.id = partner.getId();
        this.slug = partner.getSlug();
//        this.logo = partner.getLogo();
        this.active = partner.getActive();
        this.website = partner.getWebsite();
        this.orderNum= partner.getOrderNum();
    }

}
