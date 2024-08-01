package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.ContactForm;
import uz.imed.entity.translation.ContactFormTranslation;
import uz.imed.exeptions.NotFoundException;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactFormDTO {

    Long id;

    String phoneNumber;

    String email;

    String location;

    String workTime;

    public ContactFormDTO(ContactForm contactFrom, String lang){
        this.id=contactFrom.getId();
        this.phoneNumber=contactFrom.getPhoneNumber();
        this.email=contactFrom.getEmail();
        ContactFormTranslation contactFromTranslation=TranslationHelper.getTranslationByLang(lang,contactFrom.getTranslations());
        this.location=contactFromTranslation.getLocation();
        this.workTime=contactFromTranslation.getWorkTime();
    }

    static class TranslationHelper {
        static ContactFormTranslation getTranslationByLang(String lang, List<ContactFormTranslation> contactTranslations) {
            return contactTranslations
                    .stream()
                    .filter(translation -> translation.getLanguage().equals(lang))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Language not supported: " + lang));
        }
    }
}
