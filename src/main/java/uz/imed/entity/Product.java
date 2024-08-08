package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "product")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(unique = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String slug;

//    @ElementCollection
//    List<String> tagUz;
//    @ElementCollection
//    List<String> tagRu;
//    @ElementCollection
//    List<String> tagEn;

    @JsonProperty(value = "new")
    Boolean aNew;

    Boolean sale;

    @Column(length = 500)
    String shortDescriptionUz;
    @Column(length = 500)
    String shortDescriptionRu;
    @Column(length = 500)
    String shortDescriptionEn;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.EAGER, orphanRemoval = true)
    List<Description> descriptions;

    @Min(value = 0, message = "Cannot have a value less than 0.")
    @Column(columnDefinition = "INT CHECK (discount >= 0 AND discount <= 100)")
    Integer discount;

    @DecimalMin("0.0")
    @Column(columnDefinition = "DOUBLE PRECISION CHECK (original_price >= 0.0)")
    Double originalPrice;

    String conditionsUz;
    String conditionsRu;
    String conditionsEn;

    @ManyToOne
    @JsonProperty(value = "brand")
    Partner partner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Catalog catalog;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Category category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    List<Characteristic> characteristics;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    List<Review> reviews;

    @OneToMany
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    List<Photo> gallery;

    Boolean active;

    Boolean popular;
}
