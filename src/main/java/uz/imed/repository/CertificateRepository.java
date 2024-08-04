package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.imed.entity.Certificate;
import uz.imed.entity.Client;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate,Long> {

    @Modifying
    @Query(value = "update certificate set slug = :slug where id = :id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long clientId);

    @Query("select c from certificate c where upper(c.slug) = upper(?1)")
    Optional<Certificate> findBySlugIgnoreCase(String slug);


}
