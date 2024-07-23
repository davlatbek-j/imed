package uz.imed.payload;



import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Product;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {

    String name;

    String mainDescription;

    String slug;

    boolean active;

    String mainPhotoUrl;

    public ProductDTO(Product product) {
        this.name = product.getName();
        this.mainDescription = product.getMainDescription();
        this.slug = product.getSlug();
        this.active = product.isActive();
        this.mainPhotoUrl = product.getMainPhotoUrl();
    }

}