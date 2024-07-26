package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {



}
