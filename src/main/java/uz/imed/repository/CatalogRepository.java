package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.imed.entity.Catalog;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long>, JpaSpecificationExecutor<Catalog> {


    @Modifying
    @Query(value = "UPDATE catalog SET name = :name WHERE id = :id", nativeQuery = true)
    void updateName(Long id, String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM category_item_catalog_list WHERE catalog_list_id= :id", nativeQuery = true)
    void deleteCascade(Long id);
}
