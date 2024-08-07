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

@Entity
@Table(name = "category")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nameUz;
    String nameRu;
    String nameEn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(unique = true)
    String slug;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Photo photo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", orphanRemoval = true)
    List<Catalog> catalogs;

    Boolean active = true;

    Boolean main = true;

    @PostPersist
    private void setCatalogId()
    {
        if (this.catalogs != null)
            this.catalogs.forEach(i -> i.setCategory(this));
    }
}