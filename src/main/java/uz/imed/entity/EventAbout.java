package uz.imed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "event_about")
public class EventAbout extends BaseEntity
{
    String headingUz;

    String headingRu;

    String headingEng;

    @Column(length = 1500)
    String textUz;

    @Column(length = 1500)
    String textRu;

    @Column(length = 1500)
    String textEng;

}