package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.AboutUsPageHeader;
import uz.imed.entity.Photo;
import uz.imed.exception.LanguageNotSupportException;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutUsPageHeaderDTO {

    Long id;

    String title;

    String subTitle;

    String text;

    Photo photo;

    List<AboutUsHeaderOptionDTO> options;

    public AboutUsPageHeaderDTO(AboutUsPageHeader header, String lang) {
        this.id = header.getId();
        this.photo = header.getPhoto();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.title = header.getTitleUz();
                this.subTitle = header.getSubTitleUz();
                this.text = header.getTextUz();
                break;
            }

            case "ru": {
                this.title = header.getTitleRu();
                this.subTitle = header.getSubTitleRu();
                this.text = header.getTextRu();
                break;
            }

            case "en": {
                this.title = header.getTitleEn();
                this.subTitle = header.getSubTitleEn();
                this.text = header.getTextEn();
                break;
            }
            default:
                throw new LanguageNotSupportException("Language not supported: "+lang);
        }
        this.options = header.getOptions().stream()
                .map(option -> new AboutUsHeaderOptionDTO(option, lang))
                .collect(Collectors.toList());
    }

}
