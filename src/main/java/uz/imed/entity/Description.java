package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
public class Description
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 500)
    String titleUz;
    @Column(length = 500)
    String titleRu;
    @Column(length = 500)
    String titleEn;

    @Column(length = 5000)
    String valueUz;
    @Column(length = 5000)
    String valueRu;
    @Column(length = 5000)
    String valueEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Product product;
}
