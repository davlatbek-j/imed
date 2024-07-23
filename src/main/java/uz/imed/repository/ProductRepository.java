package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.imed.entity.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(value = "update product set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id") Long id, boolean active);

    @Modifying
    @Query(value = "update product set slug = :slug where id = :id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long productId);

    @Query(value = "select slug from product where id = :id", nativeQuery = true)
    String findSlugById(@Param("id") Long productId);

    Optional<Product> findBySlug(String slug);

    @Query(value = "SELECT c.slug FROM catalog c JOIN product p ON c.id = p.catalog_id WHERE p.slug = :productSlug", nativeQuery = true)
    String findCatalogSlugByProductSlug(@Param("productSlug") String productSlug);

}
