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
@Entity(name = "client_review")
public class ClientReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String clientName;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    Photo logo;

    @Column(length = 5000)
    String commentUz;

    @Column(length = 5000)
    String commentRu;

    @Column(length = 5000)
    String commentEn;

}
