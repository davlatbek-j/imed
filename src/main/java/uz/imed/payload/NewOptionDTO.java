package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.NewOption;
import uz.imed.entity.Photo;
import uz.imed.exception.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewOptionDTO {

    Long id;

    String heading;

    String text;

    Photo photo;

    Integer orderNum;

    public NewOptionDTO(NewOption newOption, String lang) {
        this.id = newOption.getId();
        this.photo = newOption.getPhoto();
        this.orderNum = newOption.getOrderNum();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.heading = newOption.getHeadingUz();
                this.text = newOption.getTextUz();
                break;
            }

            case "ru": {
                this.heading = newOption.getHeadingRu();
                this.text = newOption.getTextRu();
                break;
            }

            case "en": {
                this.heading = newOption.getHeadingEn();
                this.text = newOption.getTextEn();
                break;
            }

            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }

}
