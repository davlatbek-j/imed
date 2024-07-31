package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.ClientTranslation;

@Repository
public interface ClientTranslationRepository extends JpaRepository<ClientTranslation, Long> {
}
