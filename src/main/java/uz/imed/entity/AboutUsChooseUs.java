package uz.imed.entity;


import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "about_us_choose_us")
public class AboutUsChooseUs extends BaseEntity {

    String nameUz;

    String nameRu;

    String nameEng;

    String descriptionUz;

    String descriptionRu;

    String descriptionEng;

    String slug;

}