package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.translation.RepresentativeTranslation;

public interface RepresentativeTranslationRepository extends JpaRepository<RepresentativeTranslation,Long> {
}
