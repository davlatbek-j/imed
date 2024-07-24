package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.PartnerHeader;

@Repository
public interface PartnerHeaderRepository extends JpaRepository<PartnerHeader, Long> {
}
