package uz.imed.entity;


import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "about_us_partner_task")
public class AboutUsPartner extends BaseEntity{

    String name;

    String iconUrl;

    boolean active;

}
