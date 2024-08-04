package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.imed.entity.AboutUsChooseUs;
import uz.imed.entity.Certificate;

import java.util.Optional;

@Repository
public interface AboutUsChooseUsRepository extends JpaRepository<AboutUsChooseUs, Long> {
    @Modifying
    @Query(value = "update about_us_choose_us set slug = :slug where id = :id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long clientId);

    @Query("select c from about_us_choose_us c where upper(c.slug) = upper(?1)")
    Optional<AboutUsChooseUs> findBySlugIgnoreCase(String slug);
}
