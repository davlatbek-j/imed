package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Client;
import uz.imed.exeptions.LanguageNotSupportException;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientDTO {

    Long id;

    String name;

    String description;

    String slug;

    String locationUrl;

    PhotoDTO icon;

    List<PhotoDTO> gallery;

    public ClientDTO(Client client,String lang){

        this.id=client.getId();
        this.slug=client.getSlug();
        this.locationUrl=client.getLocationUrl();
        this.icon=new PhotoDTO(client.getIcon());
        client.getGallery().forEach(i -> this.gallery.add(new PhotoDTO(i)));

        switch (lang.toLowerCase())
        {
            case "uz":
            {
                this.name = client.getNameUz();
                this.description=client.getDescriptionUz();
                break;
            }
            case "ru":
            {
                this.name = client.getNameRu();
                this.description=client.getDescriptionRu();
                break;
            }
            case "eng":
            {
                this.name = client.getNameEng();
                this.description=client.getDescriptionEng();
                break;
            }
            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }




}
