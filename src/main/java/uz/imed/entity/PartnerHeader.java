package uz.imed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "partner_header")
public class PartnerHeader extends BaseEntity{

    @Column(length = 3000)
    String descriptionUz;

    @Column(length = 3000)
    String descriptionRu;

    @Column(length = 3000)
    String descriptionEng;

}
