package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.AboutUsPageHeader;

@Repository
public interface AboutUsPageHeaderRepository extends JpaRepository<AboutUsPageHeader, Long> {

}
