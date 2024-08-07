package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.ClientReview;

@Repository
public interface ClientReviewRepository extends JpaRepository<ClientReview, Long> {
}
