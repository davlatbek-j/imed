package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.EventAbout;

@Repository
public interface EventAboutRepository extends JpaRepository<EventAbout, Long> {
}
