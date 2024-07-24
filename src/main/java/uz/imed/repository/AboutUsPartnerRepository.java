package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.imed.entity.AboutUsPartner;

@Repository
public interface AboutUsPartnerRepository extends JpaRepository<AboutUsPartner, Long> {

    @Query(value = "select icon_url from about_us_partner_task where id=:id", nativeQuery = true)
    String findPhotoUrlById(@Param("id")Long id);

    @Modifying
    @Query(value = "update about_us_partner_task set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id")Long id, boolean active);

}
