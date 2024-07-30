package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Long>
{
}
