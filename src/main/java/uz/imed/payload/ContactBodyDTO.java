package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.ContactBody;
import uz.imed.entity.translation.ContactBodyTranslation;
import uz.imed.exeptions.NotFoundException;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactBodyDTO {

    Long id;

    String phone;

    String email;

    boolean addable;

    String address;

    String schedule;

    public ContactBodyDTO(ContactBody contactBody,String lang){
        this.id=contactBody.getId();
        this.phone=contactBody.getPhone();
        this.email=contactBody.getEmail();
        this.addable=contactBody.isAddable();
        ContactBodyTranslation contactBodyTranslation=TranslationHelper.getTranslationByLang(lang,contactBody.getTranslations());
        this.address=contactBodyTranslation.getAddress();
        this.schedule=contactBodyTranslation.getSchedule();
    }

    static class TranslationHelper {
        static ContactBodyTranslation getTranslationByLang(String lang, List<ContactBodyTranslation> contactTranslations) {
            return contactTranslations
                    .stream()
                    .filter(translation -> translation.getLanguage().equals(lang))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Language not supported: " + lang));
        }
    }
}
