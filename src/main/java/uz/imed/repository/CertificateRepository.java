package uz.imed.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.imed.entity.Certificate;

import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    @Modifying
    @Transactional
    @Query(value = "update certificate set slug=:slug where id=:id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long certificateId);

    Optional<Certificate> findBySlug(String slug);

}
