package uz.imed.payload.catalog;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Category;
import uz.imed.exception.LanguageNotSupportException;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryNameDTO
{
    Long id;

    String name;

    List<CatalogNameDTO> catalogs;

    public CategoryNameDTO(Category entity, String lang)
    {
        if (entity != null)
        {
            this.id = entity.getId();
            switch (lang.toLowerCase())
            {
                case "uz" -> this.name = entity.getNameUz();
                case "ru" -> this.name = entity.getNameRu();
                case "en" -> this.name = entity.getNameEn();
                default -> throw new LanguageNotSupportException("Language not supported: " + lang);
            }
            if (entity.getCatalogs() != null)
            {
                this.catalogs = new ArrayList<>();
                entity.getCatalogs().forEach(i -> this.catalogs.add(new CatalogNameDTO(i, lang)));
            }
        }
    }
}
