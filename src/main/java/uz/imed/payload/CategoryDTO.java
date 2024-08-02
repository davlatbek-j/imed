package uz.imed.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Category;
import uz.imed.exception.LanguageNotSupportException;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO
{
    Long id;

    String slug;

    String name;

    PhotoDTO photo;

    List<CatalogDTO> catalogs;

    Boolean active;

    Boolean main;

    public CategoryDTO(Category entity, String lang)
    {
        this.id = entity.getId();
        this.slug = entity.getSlug();
        this.photo = new PhotoDTO(entity.getPhoto());
        this.active = entity.getActive();
        this.main = entity.getMain();
        switch (lang.toLowerCase())
        {
            case "uz" -> this.name = entity.getNameUz();
            case "ru" -> this.name = entity.getNameRu();
            case "en" -> this.name = entity.getNameEn();
            default -> throw new LanguageNotSupportException("Language not supported: " + lang);
        }
        this.catalogs = new ArrayList<>();
        entity.getCatalogs().forEach(i -> this.catalogs.add(new CatalogDTO(i, lang)));
    }
}
