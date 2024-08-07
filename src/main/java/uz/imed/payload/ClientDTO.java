package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Client;
import uz.imed.exception.LanguageNotSupportException;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientDTO
{

    Long id;

    String name;

    String description;

    String locationUrl;

    PhotoDTO icon;

    List<PhotoDTO> gallery;

    public ClientDTO(Client client, String lang)
    {
        this.id = client.getId();
        this.locationUrl = client.getLocationUrl();
        this.icon = new PhotoDTO(client.getLogo());
        this.gallery = new ArrayList<>();
        client.getGallery().forEach(i -> this.gallery.add(new PhotoDTO(i)));
        this.name = client.getName();

        switch (lang.toLowerCase())
        {
            case "uz" -> this.description = client.getDescriptionUz();
            case "ru" -> this.description = client.getDescriptionRu();
            case "en" -> this.description = client.getDescriptionEn();
            default -> throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }

    public ClientDTO(Client entity, String lang, int gallerySize)
    {
        this.id = entity.getId();
        this.name = entity.getName();
        this.locationUrl = entity.getLocationUrl();
        this.icon = new PhotoDTO(entity.getLogo());
        this.gallery = new ArrayList<>();

        int originalSize = entity.getGallery().size();
        for (int i = 0; i < originalSize && i < gallerySize; i++)
            this.gallery.add(new PhotoDTO(entity.getGallery().get(i)));

        switch (lang.toLowerCase())
        {
            case "uz" -> this.description = entity.getDescriptionUz();
            case "ru" -> this.description = entity.getDescriptionRu();
            case "en" -> this.description = entity.getDescriptionEn();
            default -> throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }
}
