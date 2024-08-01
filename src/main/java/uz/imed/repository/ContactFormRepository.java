package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.ContactForm;

public interface ContactFormRepository extends JpaRepository<ContactForm,Long> {
}
