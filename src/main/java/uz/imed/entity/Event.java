package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nameUz;

    String nameRu;

    String nameEn;

    @Column(unique = true)
    String slug;

    @OneToOne
    @JsonProperty(value = "photo", access = JsonProperty.Access.READ_ONLY)
    Photo photo;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventAbout> abouts;

    String organizerUz;

    String organizerRu;

    String organizerEn;

    String countryUz;

    String countryRu;

    String countryEn;

    String dateFromUz;

    String dateFromRu;

    String dateFromEn;

    String dateToUz;

    String dateToRu;

    String dateToEn;

    String timeFrom;

    String timeTo;

    String addressUz;

    String addressEn;

    String addressRu;

    String participationUz;

    String participationRu;

    String participationEn;

    String phoneNum;

    String email;

    @PostPersist
    @PostUpdate
    private void setIdToAbout() {
        if (abouts != null) {
            this.abouts.forEach(i -> i.setEvent(this));
        }
    }

}
