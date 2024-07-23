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
public class ProductForListDTO {

    String name;

    String mainDescription;

    String slug;

    boolean active;

    String mainPhotoUrl;

    boolean isNew;

    boolean promotion;

    double price;

    double sale;

    double salePrice;

    public ProductForListDTO(Product product) {

        this.name = product.getName();
        this.mainDescription = product.getMainDescription();
        this.slug = product.getSlug();
        this.active = product.isActive();
        this.mainPhotoUrl = product.getMainPhotoUrl();
        this.isNew = product.isNew();
        this.promotion = product.isPromotion();
        this.price = product.getPrice();
        this.sale = product.getSale();
        this.salePrice= product.getSalePrice();
    }

}
