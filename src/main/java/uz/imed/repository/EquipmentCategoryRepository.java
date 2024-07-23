package uz.imed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.imed.entity.EquipmentCategory;

import java.util.Optional;

@Repository
public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {

    @Query(value = "select photo_url from equipment_category where id=:id", nativeQuery = true)
    String findPhotoUrlById(@Param("id") Long id);

    @Modifying
    @Query(value = "update equipment_category set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id") Long id, boolean active);

    @Modifying
    @Query(value = "update equipment_category set slug = :slug where id = :id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long categoryId);

    @Query(value = "select slug from equipment_category where id = :id", nativeQuery = true)
    String findSlugById(@Param("id") Long categoryId);

    @Query(value = "select name from equipment_category where id=:id", nativeQuery = true)
    String findNameById(@Param("id") Long categoryId);


    @Query(value = "select * from equipment_category where slug = :slug", nativeQuery = true)
    Optional<EquipmentCategory> findBySlug(@Param("slug") String slug);

    Optional<EquipmentCategory> findByName(String name);

}
