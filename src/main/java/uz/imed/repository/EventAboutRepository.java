package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.imed.entity.EventAbout;

@Repository
public interface EventAboutRepository extends JpaRepository<EventAbout, Long>
{

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM event_about WHERE id= :id", nativeQuery = true)
    void delete(Long id);
}
