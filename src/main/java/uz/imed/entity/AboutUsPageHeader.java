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
@Entity(name = "about_us_header")
public class AboutUsPageHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String titleUz;

    String titleRu;

    String titleEn;

    String subTitleUz;

    String subTitleRu;

    String subTitleEn;

    @Column(length = 3000)
    String textUz;

    @Column(length = 3000)
    String textRu;

    @Column(length = 3000)
    String textEn;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    Photo photo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "header", orphanRemoval = true)
    List<AboutUsHeaderOption> options;

    @PostPersist
    @PostUpdate
    private void setOptionsId() {
        if (this.options != null)
            this.options.forEach(i -> i.setHeader(this));
    }

}
