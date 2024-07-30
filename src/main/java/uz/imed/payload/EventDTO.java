package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Event;
import uz.imed.exeptions.LanguageNotSupportException;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDTO {

    Long id;

    String heading;

    String slug;

    PhotoDTO photoDTO;

    List<EventAboutDTO> eventAboutDTOS;

    String dateFrom;
    String dateTo;

    String timeFrom;
    String timeTo;

    String organizer;

    String city;

    String address;

    public EventDTO(Event event,String lang){
        this.id=event.getId();
        this.slug=event.getSlug();
        this.photoDTO=new PhotoDTO(event.getCoverPhoto());
        this.dateFrom=event.getDateFrom();
        this.dateTo=event.getDateTo();
        this.timeFrom=event.getTimeFrom();
        this.timeTo=event.getTimeTo();
        this.eventAboutDTOS=new ArrayList<>();
        event.getAbouts().forEach(i->this.eventAboutDTOS.add(new EventAboutDTO(i,lang)));
        this.city=event.getCity();

        switch (lang.toLowerCase()){

            case "uz":
            {
                this.heading=event.getHeadingUz();
                this.organizer=event.getOrganizerUz();
                this.address=event.getAddressUz();
                break;
            }

            case "ru":
            {
                this.heading=event.getHeadingRu();
                this.organizer=event.getOrganizerRu();
                this.address=event.getAddressEng();
                break;
            }

            case "eng":
            {
                this.heading=event.getHeadingEng();
                this.organizer=event.getOrganizerEng();
                this.address=event.getAddressEng();
                break;
            }
            default:{
                throw new LanguageNotSupportException("Language not supported: " + lang);
            }
        }

    }


}
