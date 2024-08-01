package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Event;
import uz.imed.entity.Photo;
import uz.imed.entity.translation.EventTranslation;
import uz.imed.exeptions.NotFoundException;


import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDTO {

    Long id;

    String slug;

    String dateFrom;
    String dateTo;

    String timeFrom;
    String timeTo;

    String city;

    Photo coverPhoto;

    String heading;

    String organizer;

    String address;

    String text;

    public EventDTO(Event event, String lang) {
        this.id = event.getId();
        this.slug=event.getSlug();
        this.dateFrom=event.getDateFrom();
        this.dateTo=event.getDateTo();
        this.timeFrom=event.getTimeFrom();
        this.timeTo=event.getTimeTo();
        this.city=event.getCity();
        this.coverPhoto=event.getCoverPhoto();

        EventTranslation eventTranslation=TranslationHelper.getTranslationByLang(lang,event.getEventTranslations());
        this.heading=eventTranslation.getHeading();
        this.organizer= eventTranslation.getOrganizer();
        this.address=eventTranslation.getAddress();
        this.text=eventTranslation.getText();


    }

    static class TranslationHelper {
        static EventTranslation getTranslationByLang(String lang, List<EventTranslation> clientTranslations) {
            return clientTranslations
                    .stream()
                    .filter(translation -> translation.getLanguage().equals(lang))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Language not supported: " + lang));

        }
    }


}
