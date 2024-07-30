package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.EventAbout;
import uz.imed.exeptions.LanguageNotSupportException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventAboutDTO {

    Long id;

    String heading;

    String text;

    public  EventAboutDTO(EventAbout eventAbout,String lang){
        this.id=eventAbout.getId();
        switch (lang.toLowerCase()){
            case "uz":
            {
                this.heading=eventAbout.getHeadingUz();
                this.text=eventAbout.getTextUz();
                break;
            }
            case "ru":
            {
                this.heading=eventAbout.getHeadingRu();
                this.text=eventAbout.getHeadingRu();
                break;
            }
            case "eng":
            {
                this.heading=eventAbout.getHeadingEng();
                this.text=eventAbout.getTextEng();
                break;
            }
            default:{
                throw new LanguageNotSupportException("Language not supported: " + lang);
            }
        }
    }

}
