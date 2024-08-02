package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.translations.CatalogTranslations;

public interface CatalogTranslationsRepository extends JpaRepository<CatalogTranslations, Integer>
{
}
