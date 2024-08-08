package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Event;
import uz.imed.entity.Photo;
import uz.imed.exception.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventForListDTO {

    Long id;

    String slug;

    String name;

    String address;

    Photo photo;

    public EventForListDTO(Event event, String lang) {
        this.id = event.getId();
        this.slug = event.getSlug();
        this.photo = event.getPhoto();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.name = event.getNameUz();
                this.address = event.getAddressUz();
                break;
            }

            case "ru": {
                this.name = event.getNameRu();
                this.address = event.getAddressRu();
                break;
            }

            case "en": {
                this.name = event.getNameEn();
                this.address = event.getAddressEn();
                break;
            }
            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }
}
