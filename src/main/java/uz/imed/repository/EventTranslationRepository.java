package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.translation.EventTranslation;


public interface EventTranslationRepository extends JpaRepository<EventTranslation,Long> {

}
