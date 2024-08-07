package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.imed.entity.CommercialOffer;

public interface CommercialOfferRepository extends JpaRepository<CommercialOffer, Long>
{
}
