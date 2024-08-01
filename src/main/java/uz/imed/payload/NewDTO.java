package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.New;
import uz.imed.entity.Photo;
import uz.imed.entity.translation.EventTranslation;
import uz.imed.entity.translation.NewTranslation;
import uz.imed.exeptions.LanguageNotSupportException;
import uz.imed.exeptions.NotFoundException;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewDTO {

    Long id;

    Photo photo;

    String slug;

    Date createDate;

    boolean active;

    Integer orderNum;

    String heading;

    String text;


    public NewDTO(New newness, String lang) {
        this.id = newness.getId();
        this.createDate = newness.getCreateDate();
        this.slug = newness.getSlug();
        this.active = newness.getActive();
        this.photo=newness.getPhoto();
        this.orderNum=newness.getOrderNum();

        NewTranslation newTranslation=TranslationHelper.getTranslationByLang(lang,newness.getTranslations());
        this.heading=newTranslation.getHeading();
        this.text=newTranslation.getText();


    }

    static class TranslationHelper {
        static NewTranslation getTranslationByLang(String lang, List<NewTranslation> newTranslations) {
            return newTranslations
                    .stream()
                    .filter(translation -> translation.getLanguage().equals(lang))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Language not supported: " + lang));
        }
    }

}
