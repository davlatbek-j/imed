package uz.imed.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
// Single entity
public class Navbar
{
    @Id
    Integer id = 1;

    String logo;

    String searchIcon;

    String favouriteIcon;

    String phoneIcon;

    String phone;

    @OneToMany(cascade = CascadeType.ALL)
    List<NavbarOption> option;
}
