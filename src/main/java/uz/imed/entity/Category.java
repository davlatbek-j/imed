package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.translations.CategoryTranslations;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "category")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(unique = true)
    String slug;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Photo photo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoryItem", orphanRemoval = true)
    @JsonProperty(value = "catalog")
    List<Catalog> catalogs;

    Boolean active = true;

    Boolean main = true;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", orphanRemoval = true)
    List<CategoryTranslations> translations;

    @PostPersist
    private void setCatalogId()
    {
        if (this.catalogs != null)
            this.catalogs.forEach(i -> i.setCategoryItem(this));

        if (this.translations != null)
            this.translations.forEach(i -> i.setCategory(this));
    }

}