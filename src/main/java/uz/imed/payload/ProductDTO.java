package uz.imed.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Product;
import uz.imed.exception.LanguageNotSupportException;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO
{
    Long id;

    String name;

    String slug;

    List<String> tag;

    String shortDescription;

    List<DescriptionDTO> descriptions;

    @Min(value = 0, message = "Cannot have a value less than 0.")
    @Max(value = 100)
    Integer discount;

    @DecimalMin("0.0")
    Double originalPrice;

    String conditions;

    @JsonProperty(value = "brand")
    PartnerLogoNameDTO partner;

//    CatalogDTO catalog;

//    CategoryDTO category;

    List<CharacteristicDTO> characteristics;

    List<ReviewDTO> reviews;

    List<PhotoDTO> gallery;

    Boolean active;

    Boolean popular;

    public ProductDTO(Product entity, String lang)
    {
        if (entity == null)
            return;

        this.id = entity.getId();
        this.name = entity.getName();
        this.slug = entity.getSlug();
        this.descriptions = new ArrayList<>();
        entity.getDescriptions().forEach(i -> descriptions.add(new DescriptionDTO(i, lang)));
        this.discount = entity.getDiscount();
        this.originalPrice = entity.getOriginalPrice();
        this.partner = new PartnerLogoNameDTO(entity.getPartner());
//        this.catalog = new CatalogDTO(entity.getCatalog(), lang);
//        this.category = new CategoryDTO(entity.getCategory(), lang);
        this.characteristics = new ArrayList<CharacteristicDTO>();
        entity.getCharacteristics().forEach(i -> this.characteristics.add(new CharacteristicDTO(i, lang)));
        this.reviews = new ArrayList<>();
        entity.getReviews().forEach(i -> this.reviews.add(new ReviewDTO(i, lang)));
        this.gallery = new ArrayList<>();
        entity.getGallery().forEach(i -> this.gallery.add(new PhotoDTO(i)));
        this.active = entity.getActive();
        this.popular = entity.getPopular();

        switch (lang.toLowerCase())
        {
            case "uz" ->
            {
                this.tag = entity.getTagUz();
                this.shortDescription = entity.getShortDescriptionUz();
                this.conditions = entity.getConditionsUz();
            }
            case "ru" ->
            {
                this.tag = entity.getTagRu();
                this.shortDescription = entity.getShortDescriptionRu();
                this.conditions = entity.getConditionsRu();
            }
            case "en" ->
            {
                this.tag = entity.getTagEn();
                this.shortDescription = entity.getShortDescriptionEn();
                this.conditions = entity.getConditionsEn();
            }
            default -> throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }

}
