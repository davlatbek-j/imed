package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.Catalog;

public interface CatalogRepository extends JpaRepository<Catalog, Long>
{
}
