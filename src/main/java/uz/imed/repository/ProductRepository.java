package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.imed.entity.Catalog;
import uz.imed.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
    Optional<Product> findBySlug(String slug);

    List<Product> findAllByCategoryId(Long categoryId);

    List<Product> findAllByCategoryIdAndActive(Long categoryId, Boolean active);

    List<Product> findAllByCatalogId(Long catalogId);

    List<Product> findAllByCatalogIdAndActive(Long catalogId, Boolean active);

    List<Product> findAllByActive(Boolean active);

    List<Product> findAllByPopular(Boolean popular);

    boolean existsByPartnerId(Long id);

    Integer countByPartnerId(Long id);

}
