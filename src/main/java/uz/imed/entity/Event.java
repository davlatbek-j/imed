package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.BaseEntity;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "event")
public class Event extends BaseEntity
{

    String headingUz;

    String headingRu;

    String headingEng;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String slug;

    @OneToOne
    @JsonProperty(value = "photo", access = JsonProperty.Access.READ_ONLY)
    Photo coverPhoto; // Main photo

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventAbout> abouts;

    String dateFrom;
    String dateTo;

    String timeFrom;
    String timeTo;


    String organizerUz;
    String organizerRu;
    String organizerEng;

    String city;


    String addressUz;
    String addressRu;
    String addressEng;
}