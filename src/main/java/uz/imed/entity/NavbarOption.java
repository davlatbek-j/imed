package uz.imed.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
// Single entity
public class NavbarOption
{
    @Id
    Integer id = 1;

    String nameUz;
    String nameRu;

    String slug;
}
