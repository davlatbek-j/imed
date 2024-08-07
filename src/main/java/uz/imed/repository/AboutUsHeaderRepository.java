package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.imed.entity.AboutUsChooseUs;
import uz.imed.entity.AboutUsHeader;

import java.util.Optional;

@Repository
public interface AboutUsHeaderRepository extends JpaRepository<AboutUsHeader, Long> {



    @Query("select c from about_us_choose_us c where upper(c.slug) = upper(?1)")
    Optional<AboutUsHeader> findBySlugIgnoreCase(String slug);
}
