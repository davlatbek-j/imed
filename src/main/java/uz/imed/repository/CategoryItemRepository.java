package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.imed.entity.CategoryItem;

import java.util.List;
import java.util.Optional;

public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long>, JpaSpecificationExecutor<CategoryItem>
{
    @Query(value = "SELECT order_num FROM category_item  WHERE order_num IS NOT NULL", nativeQuery = true)
    List<Integer> findAllExistingOrders();


    @Modifying
    @Query(value = "UPDATE category_item SET title = :title WHERE id = :id", nativeQuery = true)
    void updateTitle(@Param("id") Long id, @Param("title") String title);

    @Modifying
    @Query(value = "UPDATE category_item SET slug = :slug WHERE id = :id", nativeQuery = true)
    void updateSlug(Long id, String slug);

    @Modifying
    @Query(value = "UPDATE category_item SET active = :active WHERE id = :id", nativeQuery = true)
    void updateActive(Long id, Boolean active);

    @Modifying
    @Query(value = "UPDATE category_item SET main = :main WHERE id = :id", nativeQuery = true)
    void updateMain(Long id, Boolean main);

    @Query(value = "SELECT MAX(order_num) FROM category_item", nativeQuery = true)
    Optional<Integer> getMaxOrderNum();

    @Modifying
    @Query(value = "UPDATE category_item SET order_num = NULL", nativeQuery = true)
    void clearOrderNum();

    @Modifying
    @Query(value = "UPDATE category_item SET order_num = :orderNum WHERE id = :id", nativeQuery = true)
    void updateOrderNum(Long id, Integer orderNum);

    @Query(value = "SELECT * FROM category_item WHERE slug = :slug", nativeQuery = true)
    Optional<CategoryItem> findBySlug(String slug);

    void deleteBySlug(String slug);

    @Query(value = "SELECT catalog_list_id FROM category_item_catalog_list WHERE category_item_id = :categoryItemId", nativeQuery = true)
    List<Long> getCatalogIds(Long categoryItemId);
}
