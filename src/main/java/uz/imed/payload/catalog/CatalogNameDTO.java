package uz.imed.payload.catalog;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Catalog;
import uz.imed.exception.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CatalogNameDTO
{
    Long id;

    String name;

    public CatalogNameDTO(Catalog entity, String lang)
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
        }
    }
}
