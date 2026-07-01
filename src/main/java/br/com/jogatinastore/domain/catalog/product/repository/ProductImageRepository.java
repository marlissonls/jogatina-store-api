package br.com.jogatinastore.domain.catalog.product.repository;

import br.com.jogatinastore.domain.catalog.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
}
