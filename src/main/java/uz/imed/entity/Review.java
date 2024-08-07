package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
public class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nameDoctorUz;
    String nameDoctorRu;
    String nameDoctorEn;

    String positionUz;
    String positionRu;
    String positionEn;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Photo doctorPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Product product;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "review", orphanRemoval = true)
    List<ReviewOption> options;
}
