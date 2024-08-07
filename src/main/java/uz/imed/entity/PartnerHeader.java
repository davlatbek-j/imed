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

@Entity(name = "partner_header")
public class PartnerHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 500)
    String titleUz;
    @Column(length = 500)
    String titleRu;
    @Column(length = 500)
    String titleEn;

    @Column(length = 2000)
    String textUz;
    @Column(length = 2000)
    String textRu;
    @Column(length = 2000)
    String textEn;
}
