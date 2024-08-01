package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.translation.ContactBodyTranslation;

public interface ContactBodyTranslationRepo extends JpaRepository<ContactBodyTranslation,Long> {
}
