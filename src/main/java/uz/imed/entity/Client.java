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
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nameUz;

    String nameRu;

    String nameEng;

    String slug;

    String descriptionUz;

    String descriptionRu;

    String descriptionEng;

    String locationUrl;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    Photo  icon;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Photo> gallery;
}
