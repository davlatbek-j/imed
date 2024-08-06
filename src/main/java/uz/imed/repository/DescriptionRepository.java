package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.imed.entity.Description;

public interface DescriptionRepository extends JpaRepository<Description, Long>
{
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM description WHERE id= :id", nativeQuery = true)
    void delete(Long id);
}
