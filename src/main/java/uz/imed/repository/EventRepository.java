package uz.imed.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.imed.entity.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>
{
    Optional<Event> findBySlug(String slug);

    List<Event> findByCityEqualsIgnoreCase(String city, Pageable pageable);

    @Query(value = "SELECT DISTINCT city FROM event", nativeQuery = true)
    List<String> getCity();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM event")
    void deleteCascade(Long aboutId);
}
