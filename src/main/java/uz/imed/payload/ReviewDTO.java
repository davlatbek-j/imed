package uz.imed.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Review;
import uz.imed.exception.LanguageNotSupportException;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDTO
{
    Long id;

    String nameDoctor;

    String position;

    PhotoDTO doctorPhoto;

    List<ReviewOptionsDTO> options;

    public ReviewDTO(Review entity, String lang)
    {
        if (entity == null)
            return;

        this.id = entity.getId();
        this.doctorPhoto = new PhotoDTO(entity.getDoctorPhoto());
        this.options = new ArrayList<>();
        entity.getOptions().forEach(i -> this.options.add(new ReviewOptionsDTO(i, lang)));

        switch (lang.toLowerCase())
        {
            case "uz" ->
            {
                this.nameDoctor = entity.getNameDoctorUz();
                this.position = entity.getPositionUz();
            }
            case "en" ->
            {
                this.nameDoctor = entity.getNameDoctorEn();
                this.position = entity.getPositionEn();
            }
            case "ru" ->
            {
                this.nameDoctor = entity.getNameDoctorRu();
                this.position = entity.getPositionRu();
            }
            default -> throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }
}
