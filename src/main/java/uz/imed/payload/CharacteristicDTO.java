package uz.imed.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Characteristic;
import uz.imed.exception.LanguageNotSupportException;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CharacteristicDTO
{
    Long id;

    String title;

    String value;

    public CharacteristicDTO(Characteristic entity, String lang)
    {
        this.id = entity.getId();
        switch (lang.toLowerCase())
        {
            case "uz" ->
            {
                this.title = entity.getTitleUz();
                this.value = entity.getTitleUz();
            }
            case "en" ->
            {
                this.title = entity.getTitleEn();
                this.value = entity.getTitleEn();
            }
            case "ru" ->
            {
                this.title = entity.getTitleRu();
                this.value = entity.getTitleRu();
            }
            default -> throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }
}
