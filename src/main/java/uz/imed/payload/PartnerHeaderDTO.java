package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.PartnerHeader;
import uz.imed.exception.LanguageNotSupportException;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerHeaderDTO {

    Long id;

    String title;

    String text;

    public PartnerHeaderDTO(PartnerHeader entity, String lang) {
        this.id = entity.getId();
        switch (lang.toLowerCase()) {
            case "uz" -> {
                this.title = entity.getTitleUz();
                this.text = entity.getTitleUz();
            }
            case "en" -> {
                this.title = entity.getTitleEn();
                this.text = entity.getTitleEn();
            }
            case "ru" -> {
                this.title = entity.getTitleRu();
                this.text = entity.getTitleRu();
            }
            default -> throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }
}
