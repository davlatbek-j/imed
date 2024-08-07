package uz.imed.entity;

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
@Entity(name = "certificate")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String slug;

    String titleUz;

    String titleRu;

    String titleEn;

    @Column(length = 5000)
    String textUz;

    @Column(length = 5000)
    String textRu;

    @Column(length = 5000)
    String textEn;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    Photo photo;

    Boolean active;

}
