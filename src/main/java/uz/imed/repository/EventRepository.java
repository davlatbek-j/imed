package uz.imed.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.imed.entity.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Modifying
    @Transactional
    @Query(value = "update event set slug=:slug where id=:id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long id);

    Optional<Event> findBySlug(String slug);

    @Query(value = "SELECT * FROM event WHERE LOWER(address_uz)=LOWER(:address) OR LOWER(address_ru)=LOWER(:address) OR LOWER(address_en)=LOWER(:address)",nativeQuery = true)
    List<Event> findAllByAddress(@Param(value = "address") String address);

    List<Event> findAllByAddressRuContainingIgnoreCase(String addressRu);
}
