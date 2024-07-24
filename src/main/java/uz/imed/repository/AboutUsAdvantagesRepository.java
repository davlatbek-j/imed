package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.imed.entity.AboutUsAdvantages;

public interface AboutUsAdvantagesRepository extends JpaRepository<AboutUsAdvantages,Long> {
    @Query(value = "select icon_url from about_us_advantages where id=:id", nativeQuery = true)
    String findPhotoUrlById(@Param("id")Long id);
}
