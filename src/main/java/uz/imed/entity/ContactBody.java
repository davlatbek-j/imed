package uz.imed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.translation.ContactBodyTranslation;
import uz.imed.entity.translation.EventTranslation;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "contact_body")
public class ContactBody  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String phone;

    String email;

    boolean addable;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    List<ContactBodyTranslation> translations;

}
