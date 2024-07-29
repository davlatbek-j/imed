package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "partner")
public class Partner extends BaseEntity {

    String titleUz;
    String titleRu;
    String titleEng;

    @JsonProperty("main_description_uz")
    @Column(name = "main_description_uz")
    String mainDescriptionUz;

    @JsonProperty("main_description_ru")
    @Column(name = "main_description_ru")
    String mainDescriptionRu;

    @JsonProperty("main_description_eng")
    @Column(name = "main_description_eng")
    String mainDescriptionEng;

    @Column(length = 1000)
    String descriptionUz;

    @Column(length = 1000)
    String descriptionRu;

    @Column(length = 1000)
    String descriptionEng;

    @OneToOne
    Photo photo;

    @Column(unique = true)
    String slug;

    boolean active;

}
