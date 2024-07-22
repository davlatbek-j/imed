package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.imed.entity.Banner;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner,Long> {
    @Query(value = "select photo_url from banner where id=:id", nativeQuery = true)
    String findPhotoUrlById(@Param("id")Long id);

    @Modifying
    @Query(value = "update banner set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id")Long id, boolean active);

    @Query(value = "select sliders_id  from banner_sliders", nativeQuery = true)
    List<Long> getSlidersId();
}
