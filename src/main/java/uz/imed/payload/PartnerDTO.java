package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Partner;
import uz.imed.exception.LanguageNotSupportException;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PartnerDTO {

    Long id;

    String slug;

    PhotoDTO logo;

    String name;

    String note;

    String about;

    String website;

    Integer orderNum;

    Boolean active;

    public PartnerDTO(Partner entity, String lang) {
        this.id = entity.getId();
        this.slug = entity.getSlug();
        this.logo = new PhotoDTO(entity.getLogo());
        this.name = entity.getName();
        this.website = entity.getWebsite();
        this.orderNum = entity.getOrderNum();
        this.active = entity.getActive();
        switch (lang.toLowerCase()) {
            case "uz" -> {
                this.note = entity.getNoteUz();
                this.about = entity.getAboutUz();
            }
            case "ru" -> {
                this.note = entity.getNoteRu();
                this.about = entity.getAboutRu();
            }
            case "en" -> {
                this.note = entity.getNoteEn();
                this.about = entity.getAboutEn();
            }
            default -> throw new LanguageNotSupportException("Language not supported: " + lang);
        }
    }

}
