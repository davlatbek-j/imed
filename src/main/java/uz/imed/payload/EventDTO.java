package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Event;
import uz.imed.entity.Photo;
import uz.imed.exception.LanguageNotSupportException;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDTO {

    Long id;

    String name;

    String slug;

    Photo photo;

    List<EventAboutDTO> abouts;

    String organizer;

    String country;

    String dateFrom;

    String dateTo;

    String timeFrom;

    String timeTo;

    String address;

    String participation;

    String phoneNum;

    String email;

    public EventDTO(Event event, String lang) {
        this.id = event.getId();
        this.slug = event.getSlug();
        this.photo = event.getPhoto();
        this.timeFrom = event.getTimeFrom();
        this.timeTo = event.getTimeTo();
        this.phoneNum = event.getPhoneNum();
        this.email = event.getEmail();

        switch (lang.toLowerCase()) {

            case "uz": {
                this.name = event.getNameUz();
                this.organizer = event.getOrganizerUz();
                this.country = event.getCountryUz();
                this.dateFrom = event.getDateFromUz();
                this.dateTo = event.getDateToUz();
                this.address = event.getAddressUz();
                this.participation = event.getParticipationUz();
                break;
            }

            case "ru": {
                this.name = event.getNameRu();
                this.organizer = event.getOrganizerRu();
                this.country = event.getCountryRu();
                this.dateFrom = event.getDateFromRu();
                this.dateTo = event.getDateToRu();
                this.address = event.getAddressRu();
                this.participation = event.getParticipationRu();
                break;
            }

            case "en": {
                this.name = event.getNameEn();
                this.organizer = event.getOrganizerEn();
                this.country = event.getCountryEn();
                this.dateFrom = event.getDateFromEn();
                this.dateTo = event.getDateToEn();
                this.address = event.getAddressEn();
                this.participation = event.getParticipationEn();
                break;
            }

            default:
                throw new LanguageNotSupportException("Language not supported: " + lang);

        }
        this.abouts = event.getAbouts().stream()
                .map(eventAbout -> new EventAboutDTO(eventAbout, lang))
                .collect(Collectors.toList());
    }

}
