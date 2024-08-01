package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.translation.ContactFormTranslation;

public interface ContactFormTranslationRepo extends JpaRepository<ContactFormTranslation,Long> {
}
