package uz.imed.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Catalog;
import uz.imed.exception.LanguageNotSupportException;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CatalogDTO
{
    Long id;

    String name;

    public CatalogDTO(Catalog entity, String lang)
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
