package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

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

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    Date createdDate;

}
