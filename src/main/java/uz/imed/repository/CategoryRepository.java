package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>
{
}
