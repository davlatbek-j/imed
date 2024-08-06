package uz.imed.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Description;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DescriptionDTO
{
    Long id;

    String title;

    String value;

    public DescriptionDTO(Description entity, String lang)
    {
        if (entity != null)
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
                    this.value = entity.getValueEn();
                }
                case "ru" ->
                {
                    this.title = entity.getTitleRu();
                    this.value = entity.getValueRu();
                }
                default -> throw new RuntimeException("Language not found: " + lang);
            }
        }
    }
}
