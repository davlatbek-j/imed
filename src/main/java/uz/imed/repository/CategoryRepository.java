package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>
{
    boolean existsBySlugIgnoreCase(String slug);

    Optional<Category> findBySlugIgnoreCase(String slug);
}
