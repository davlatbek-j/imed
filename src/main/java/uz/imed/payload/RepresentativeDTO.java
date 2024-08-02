package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Photo;
import uz.imed.entity.Representative;
import uz.imed.entity.translation.NewTranslation;
import uz.imed.entity.translation.RepresentativeTranslation;
import uz.imed.exeptions.NotFoundException;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RepresentativeDTO {

    Long id;

    String email;

    String phone;

    boolean active;

    boolean addable;

    Photo photo;

    String name;

    String address;

    String country;

    String schedule;

    public RepresentativeDTO(Representative representative,String lang){
        this.id=representative.getId();
        this.email=representative.getEmail();
        this.photo=representative.getPhoto();
        this.phone=representative.getPhone();
        this.active=representative.isActive();
        this.addable= representative.isAddable();
        RepresentativeTranslation representativeTranslation=TranslationHelper.getTranslationByLang(lang,representative.getTranslations());
        this.name=representativeTranslation.getName();
        this.address=representativeTranslation.getAddress();
        this.country=representativeTranslation.getCountry();
        this.schedule=representativeTranslation.getSchedule();
    }


    static class TranslationHelper {
        static RepresentativeTranslation getTranslationByLang(String lang, List<RepresentativeTranslation> newTranslations) {
            return newTranslations
                    .stream()
                    .filter(translation -> translation.getLanguage().equals(lang))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Language not supported: " + lang));
        }
    }
}
