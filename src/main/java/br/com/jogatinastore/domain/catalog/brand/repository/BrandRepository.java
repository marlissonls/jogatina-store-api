package br.com.jogatinastore.domain.catalog.brand.repository;

import br.com.jogatinastore.domain.catalog.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {

    List<Brand> findByActiveTrue();

    Optional<Brand> findBySlugAndActiveTrue(String slug);

    boolean existsByTitle(String title);

    boolean existsBySlug(String slug);

    @Modifying
    @Query("UPDATE Brand b " +
            "SET b.active = false " +
            "WHERE b.id=:id " +
            "AND b.active")
    int deactivate(@Param("id") UUID id);

    @Modifying
    @Query("UPDATE Brand b " +
            "SET b.active = true " +
            "WHERE b.id=:id " +
            "AND NOT b.active")
    int activate(@Param("id") UUID id);
}
