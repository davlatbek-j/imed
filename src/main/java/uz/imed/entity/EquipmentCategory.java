package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Entity(name = "equipment_category")
public class EquipmentCategory extends BaseEntity {

    @Column(unique = true)
    String name;

    String photoUrl;

    @Column(unique = true)
    String slug;

    boolean active;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)

    List<Catalog> catalogList;

}