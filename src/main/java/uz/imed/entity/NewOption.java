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
@Entity(name = "new_option")
public class NewOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String headingUz;

    String headingRu;

    String headingEn;

    @Column(length = 3000)
    String textUz;

    @Column(length = 3000)
    String textRu;

    @Column(length = 3000)
    String textEn;

    @OneToOne
    Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    New newness;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    New news;

    Integer orderNum;
}
