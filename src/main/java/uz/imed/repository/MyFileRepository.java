package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.imed.entity.MyFile;

import java.util.Optional;

public interface MyFileRepository extends JpaRepository<MyFile, Long>
{
    Optional<MyFile> findByName(String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM my_file WHERE id= :id", nativeQuery = true)
    void delete(Long id);
}
