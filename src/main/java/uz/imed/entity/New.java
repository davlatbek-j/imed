package uz.imed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "news")
public class New extends BaseEntity {

    String title;

    @Column(length = 5000)
    String body;

    String date;

    String mainPhotoUrl;

    @ElementCollection
    List<String> photoUrls;

    @Column(unique = true)
    String slug;

    boolean active;

}
