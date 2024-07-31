package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.PartnerHeaderTranslation;

@Repository
public interface PartnerHeaderTranslationRepository extends JpaRepository<PartnerHeaderTranslation, Long> {
}
