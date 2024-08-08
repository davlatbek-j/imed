package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.imed.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>
{

    Photo findByIdOrName(Long id, String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM photo WHERE id= :id", nativeQuery = true)
    void delete(Long id);
}
