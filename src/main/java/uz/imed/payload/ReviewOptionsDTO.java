package uz.imed.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.ReviewOption;
import uz.imed.exception.LanguageNotSupportException;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewOptionsDTO
{
    Long id;

    String title;

    String value;

    public ReviewOptionsDTO(ReviewOption entity, String lang)
    {
        if (entity == null)
            return;

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
