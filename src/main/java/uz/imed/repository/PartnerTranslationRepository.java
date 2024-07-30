package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.translations.PartnerTranslation;

public interface PartnerTranslationRepository extends JpaRepository<PartnerTranslation, Long>
{
}
