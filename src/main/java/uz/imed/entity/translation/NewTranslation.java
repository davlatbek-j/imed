package uz.imed.entity.translation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.New;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "new_translation")
public class NewTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String heading;

    @Column(length = 5000)
    String text;

    @ManyToOne
    @JsonIgnore
    New newness;

    @Column(nullable = false)
    String language;

}
