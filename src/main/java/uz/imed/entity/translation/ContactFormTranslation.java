package uz.imed.entity.translation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.ContactForm;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "contact_from_translation")
public class ContactFormTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String location;

    String workTime;

    @ManyToOne
    @JsonIgnore
    ContactForm contactFrom;

    @Column(nullable = false)
    String language;
}
