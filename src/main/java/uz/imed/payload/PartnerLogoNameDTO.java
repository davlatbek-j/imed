package uz.imed.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Partner;
import uz.imed.entity.Photo;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerLogoNameDTO {
    Long id;
    String name;
    Photo logo;

    public PartnerLogoNameDTO(Partner entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.logo = entity.getLogo();
    }
}
