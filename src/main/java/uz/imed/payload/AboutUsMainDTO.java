package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.AboutUsHeader;
import uz.imed.entity.Photo;


import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class AboutUsMainDTO {

    Long id;

    String formName;

    String description;

    Photo photo;

    public AboutUsMainDTO(AboutUsHeader aboutUsHeader){
        this.id= aboutUsHeader.getId();
        this.formName=aboutUsHeader.getFormName();
        this.description=aboutUsHeader.getDescription();
        this.photo=aboutUsHeader.getPhoto();
    }

}
