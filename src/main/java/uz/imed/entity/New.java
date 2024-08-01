package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import uz.imed.entity.BaseEntity;
import uz.imed.entity.translation.NewTranslation;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "news")
public class New
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    Photo photo;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "newness")
    List<NewTranslation> translations;

    @Column(unique = true)
    String slug;

    Integer orderNum;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    Date createDate;

    Boolean active;
}