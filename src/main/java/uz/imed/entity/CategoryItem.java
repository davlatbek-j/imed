package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "category_item")
public class CategoryItem extends BaseEntity {

    String titleUz;
    String titleRu;
    String titleEng;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(unique = true)
    String slug;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Photo photo;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonProperty(value = "catalog")
    List<Catalog> catalogList;

    Boolean active = true;

    Boolean main = true;

    Integer orderNum;

}