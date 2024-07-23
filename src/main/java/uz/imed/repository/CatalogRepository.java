package uz.imed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.imed.entity.Catalog;

import java.util.Optional;


@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {


    @Modifying
    @Query(value = "update catalog set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id") Long id, boolean active);

    @Modifying
    @Query(value = "update catalog set slug = :slug where id = :id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long catalogId);

    @Query(value = "select slug from catalog where id = :id", nativeQuery = true)
    String findSlugById(@Param("id") Long catalogId);


    @Query(value = "select * from catalog where slug = :slug", nativeQuery = true)
    Optional<Catalog> findBySlug(@Param("slug") String slug);

    Optional<Catalog> findByNameAndCategory_Id(String name,Long categoryId);


}