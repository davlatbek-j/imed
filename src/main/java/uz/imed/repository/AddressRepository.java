package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
