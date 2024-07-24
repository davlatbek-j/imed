package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.AboutUsHeader;

@Repository
public interface AboutUsHeaderRepository extends JpaRepository<AboutUsHeader, Long> {
}
