package uz.imed.payload;




import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Catalog;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CatalogDTO {

    String name;

    String slug;

    boolean active;

    String category;

    public CatalogDTO(Catalog catalog){
        this.name=catalog.getName();
        this.slug=catalog.getSlug();
        this.active=catalog.isActive();
        this.category=catalog.getCategory().getName();
    }

}