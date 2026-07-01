package br.com.jogatinastore.domain.catalog.product.repository;

import br.com.jogatinastore.domain.catalog.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    boolean existsByTitle(String title);

    boolean existsBySlug(String slug);

    boolean existsByBarcode(String barcode);

    boolean existsBySku(String sku);

    @Modifying
    @Query("UPDATE Product p " +
            "SET p.active = false " +
            "WHERE p.id=:id " +
            "AND p.active")
    int deactivate(@Param("id") UUID id);

    @Modifying
    @Query("UPDATE Product p " +
            "SET p.active = true " +
            "WHERE p.id=:id " +
            "AND NOT p.active")
    int activate(@Param("id") UUID id);
}
