package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import uz.imed.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>
{
    @Transactional
    @Modifying
    void deleteById(Long id);
}
