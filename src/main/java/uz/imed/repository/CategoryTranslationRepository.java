package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.translations.CategoryTranslation;

public interface CategoryTranslationRepository extends JpaRepository<CategoryTranslation, Long>
{
}
