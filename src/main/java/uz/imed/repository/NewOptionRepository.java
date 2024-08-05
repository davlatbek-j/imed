package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.NewOption;

@Repository
public interface NewOptionRepository extends JpaRepository<NewOption, Long> {
}
