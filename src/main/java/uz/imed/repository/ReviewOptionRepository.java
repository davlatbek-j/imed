package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.imed.entity.ReviewOption;

public interface ReviewOptionRepository extends JpaRepository<ReviewOption, Long>
{

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM review_option WHERE id= :id", nativeQuery = true)
    void deleteByOptionId(Long id);
}
