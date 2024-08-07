package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.AboutUsHeaderOption;

@Repository
public interface AboutUsHeaderOptionRepository extends JpaRepository<AboutUsHeaderOption, Long> {
}
