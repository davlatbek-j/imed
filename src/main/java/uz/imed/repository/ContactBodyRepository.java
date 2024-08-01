package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.ContactBody;

import java.util.List;

public interface ContactBodyRepository extends JpaRepository<ContactBody, Long> {
}
