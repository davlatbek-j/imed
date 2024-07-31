package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.imed.entity.Client;
import uz.imed.entity.Partner;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Modifying
    @Query(value = "update client set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id") Long id, boolean active);

    @Modifying
    @Query(value = "update client set slug = :slug where id = :id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long clientId);

    @Query(value = "select * from client where slug = :slug", nativeQuery = true)
    Optional<Client> findBySlug(@Param("slug") String slug);

    Optional<Client> findByOrderNum(Integer orderNum);

    @Query(value = "SELECT MAX(order_num) FROM client", nativeQuery = true)
    Optional<Integer> getMaxOrderNum();

    @Query(value = "select * from client order by order_num", nativeQuery = true)
    List<Client> findAllByOrderByOrderNum();

}
