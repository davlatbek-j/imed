package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.AboutUsPartner;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutUsPartnerDTO {
    Long id;

    String name;

    PhotoDTO icon;

     boolean active;

public AboutUsPartnerDTO(AboutUsPartner entity){
    this.id=entity.getId();
    this.name=entity.getName();
    this.icon=new PhotoDTO(entity.getIcon());
    this.active=entity.isActive();
}
}
