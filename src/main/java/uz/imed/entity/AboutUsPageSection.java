package uz.imed.entity;

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
@Entity(name = "about_us_page_section")
public class AboutUsPageSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String titleUz;

    String titleRu;

    String titleEn;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    Photo photo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section", orphanRemoval = true)
    List<AboutUsSectionOption> options;

    @PostPersist
    @PostUpdate
    private void setOptionsId() {
        if (this.options != null)
            this.options.forEach(i -> i.setSection(this));
    }

}
