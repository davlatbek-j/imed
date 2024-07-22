package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.Photo;

public interface PhotoRepository extends JpaRepository<Photo,Long> {

    Photo findByIdOrName(Long id, String name);
}
