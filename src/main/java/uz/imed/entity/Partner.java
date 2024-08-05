package uz.imed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity(name = "partner")
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String slug;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    Photo logo;

    //partner name in only one lang
    String name;

    @Column(length = 1000)
    String noteUz;
    @Column(length = 1000)
    String noteRu;
    @Column(length = 1000)
    String noteEn;

    @Column(length = 5000)
    String aboutUz;
    @Column(length = 5000)
    String aboutRu;
    @Column(length = 5000)
    String aboutEn;

    String website;

    Integer orderNum;

    Boolean active;
}
