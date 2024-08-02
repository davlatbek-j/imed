package uz.imed.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Photo;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotoDTO
{
    Long id;

    String url;

    public PhotoDTO(Photo entity)
    {
        if (entity != null)
        {
            this.id = entity.getId();
            this.url = entity.getHttpUrl();
        }
    }
}