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

    String slug;

    //Client name is only in 1 language
    String name;

    String descriptionUz;

    String descriptionRu;

    String descriptionEn;

    String locationUrl;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    Photo logo;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Photo> gallery;
}
