package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.BaseEntity;
import uz.imed.entity.Photo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class BannerSlider extends BaseEntity
{
    @OneToOne
    Photo photo;

    String link;

    Boolean active;

    public BannerSlider(String link, Boolean active, Photo photo)
    {
        this.link = link;
        this.active = active;
        this.photo = photo;
    }
}
