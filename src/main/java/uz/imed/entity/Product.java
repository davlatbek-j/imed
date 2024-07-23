package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.BaseEntity;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "product")
public class Product extends BaseEntity {

    String name;

    @JsonProperty("main_description")
    String mainDescription;

    @Column(length = 1000)
    String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductCharacteristic> characteristic;

    @Column(unique = true)
    String slug;

    boolean active;

    @JsonProperty("isAll")
    boolean isAll;

    @JsonProperty("isNew")
    boolean isNew;

    boolean promotion;

    double price;

    double sale;

    double salePrice;

    String mainPhotoUrl;

    @ElementCollection
    List<String> photoUrls;

    @ManyToOne
    Catalog catalog;

}