package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Entity(name = "equipment_category")
public class Category extends BaseEntity {
    String header;


    @OneToMany(cascade = CascadeType.ALL)
    @JsonProperty(value = "item")
    List<CategoryItem> itemList;

    Boolean addable = true;

    public Category(Long id, String header, List<CategoryItem> itemList, Boolean addable)
    {
        super(id);
        this.header = header;
        this.itemList = itemList;
        this.addable = addable;
    }


}
