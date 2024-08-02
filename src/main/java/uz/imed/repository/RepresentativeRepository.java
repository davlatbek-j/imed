package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.imed.entity.Representative;

public interface RepresentativeRepository extends JpaRepository<Representative,Long> {

    @Modifying
    @Query(value = "update representative set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id")Long id, boolean active);

}
