package br.com.jogatinastore.domain.catalog.category.repository;

import br.com.jogatinastore.domain.catalog.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByActiveTrue();

    Optional<Category> findBySlugAndActiveTrue(String slug);

    boolean existsByTitle(String title);

    boolean existsBySlug(String slug);

    @Modifying
    @Query("UPDATE Category c " +
            "SET c.active = false " +
            "WHERE c.id=:id " +
            "AND c.active")
    int deactivate(@Param("id") UUID id);

    @Modifying
    @Query("UPDATE Category c " +
            "SET c.active = true " +
            "WHERE c.id=:id " +
            "AND NOT c.active")
    int activate(@Param("id") UUID id);
}
