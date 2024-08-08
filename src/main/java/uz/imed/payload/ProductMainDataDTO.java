package uz.imed.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Product;
import uz.imed.exception.LanguageNotSupportException;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductMainDataDTO
{
    Long id;

    String slug;

    String name;

    String shortDescription;

    //    List<String> tag;
    Boolean aNew;

    Boolean sale;

    Double originalPrice;

    Integer discount;

    Boolean active;

    Boolean popular;

    List<PhotoDTO> gallery;

    public ProductMainDataDTO(Product entity, String lang)
    {
        this.id = entity.getId();
        this.slug = entity.getSlug();
        this.name = entity.getName();
        this.originalPrice = entity.getOriginalPrice();
        this.discount = entity.getDiscount();
        this.active = entity.getActive();
        this.popular = entity.getPopular();
        this.gallery = new ArrayList<>();
        this.aNew = entity.getANew();
        this.sale = entity.getSale();

        entity.getGallery().forEach(g -> gallery.add(new PhotoDTO(g)));

        switch (lang.toLowerCase())
        {
            case "uz" ->
            {
                this.shortDescription = entity.getShortDescriptionUz();
//                this.tag = entity.getTagUz();
            }
            case "en" ->
            {
                this.shortDescription = entity.getShortDescriptionEn();
//                this.tag = entity.getTagEn();
            }
            case "ru" ->
            {
                this.shortDescription = entity.getShortDescriptionRu();
//                this.tag = entity.getTagRu();
            }
            default -> throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }
}
