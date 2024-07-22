package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.BannerSlider;
@Repository
public interface BannerSliderRepository extends JpaRepository<BannerSlider,Long> {
}
