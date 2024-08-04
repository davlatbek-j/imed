package uz.imed.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "certificate")
public class Certificate extends BaseEntity{

    String titleUz;

    String titleRu;

    String titleEng;

    String descriptionUz;

    String descriptionRu;

    String descriptionEng;

    String slug;

    @OneToOne
    Photo certificateImage;
}
