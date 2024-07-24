package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.AboutUsChooseUs;

@Repository
public interface AboutUsChooseUsRepository extends JpaRepository<AboutUsChooseUs, Long> {
}
