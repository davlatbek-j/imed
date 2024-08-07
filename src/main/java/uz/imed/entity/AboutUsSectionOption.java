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
@Entity(name = "about_us_section_option")
public class AboutUsSectionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 1000)
    String textUz;

    @Column(length = 1000)
    String textRu;

    @Column(length = 1000)
    String textEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    AboutUsPageSection section;

}
