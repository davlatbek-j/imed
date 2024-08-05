package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.AboutUsPageSection;

@Repository
public interface AboutUsPageSectionRepository extends JpaRepository<AboutUsPageSection, Long> {

}
