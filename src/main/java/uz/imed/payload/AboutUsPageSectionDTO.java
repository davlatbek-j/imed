package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.AboutUsPageSection;
import uz.imed.entity.Photo;
import uz.imed.exception.LanguageNotSupportException;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutUsPageSectionDTO {

    Long id;

    String title;

    Photo photo;

    List<AboutUsSectionOptionDTO> options;

    public AboutUsPageSectionDTO(AboutUsPageSection section, String lang) {
        this.id = section.getId();
        this.photo = section.getPhoto();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.title = section.getTitleUz();
                break;
            }

            case "ru": {
                this.title = section.getTitleRu();
                break;
            }

            case "en": {
                this.title = section.getTitleEn();
                break;
            }

            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);

        }
        this.options = section.getOptions().stream()
                .map(sectionOption -> new AboutUsSectionOptionDTO(sectionOption, lang))
                .collect(Collectors.toList());
    }

}
