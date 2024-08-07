package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.imed.entity.Characteristic;

public interface CharacteristicRepository extends JpaRepository<Characteristic, Long>
{
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM characteristic WHERE id= :id", nativeQuery = true)
    void deleteId(Long id);
}
