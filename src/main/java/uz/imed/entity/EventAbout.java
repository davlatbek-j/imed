package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "event_about")
public class EventAbout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String titleUz;

    String titleRu;

    String titleEn;

    @Column(length = 5000)
    String textUz;

    @Column(length = 5000)
    String textRu;

    @Column(length = 5000)
    String textEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Event event;

}
