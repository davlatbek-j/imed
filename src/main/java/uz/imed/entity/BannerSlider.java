package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Banner banner;

    public BannerSlider(String link, Boolean active, Photo photo)
    {
        this.link = link;
        this.active = active;
        this.photo = photo;
    }
}
