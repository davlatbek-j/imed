package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.imed.entity.translation.NewTranslation;

import java.util.Optional;

@Repository
public interface NewTranslationRepository extends JpaRepository<NewTranslation, Long> {

    @Query(value = "SELECT MAX(order_num) FROM new_option", nativeQuery = true)
    Optional<Integer> getMaxOrderNum();

}
