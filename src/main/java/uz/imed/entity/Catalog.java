package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity(name = "catalog")
public class Catalog extends BaseEntity {

    String name;

    @Column(unique = true)
    String slug;

    boolean active;

    @ManyToOne
    @JsonIgnore
    EquipmentCategory category;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Product> productList;

}
