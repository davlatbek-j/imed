package uz.imed.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Client;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientMainDataDTO
{
    Long id;

    String slug;

    String name;

    String description;

    PhotoDTO logo;

    public ClientMainDataDTO(Client entity, String lang)
    {
        if (entity == null)
            return;

        this.id = entity.getId();
        this.slug = entity.getSlug();
        this.name = entity.getName();
        this.logo = new PhotoDTO(entity.getLogo());
        switch (lang.toLowerCase())
        {
            case "uz" -> this.description = entity.getDescriptionUz();
            case "ru" -> this.description = entity.getDescriptionRu();
            case "en" -> this.description = entity.getDescriptionEn();
        }
    }
}