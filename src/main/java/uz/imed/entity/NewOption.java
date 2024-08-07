package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
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
    @JsonIgnore
    New newness;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    New news;

    Integer orderNum;
}
