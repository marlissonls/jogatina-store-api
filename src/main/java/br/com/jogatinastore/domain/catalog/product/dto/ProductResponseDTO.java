package br.com.jogatinastore.domain.catalog.product.dto;

import br.com.jogatinastore.domain.catalog.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String title,
        String slug,
        String barcode,
        String sku,
        String description,
        BigDecimal price,
        BigDecimal salePrice,
        BigDecimal costPrice,
        BigDecimal rating,
        Integer ratingCount,
        Boolean active,
        Boolean featured,
        UUID categoryId,
        UUID brandId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ProductResponseDTO(Product product) {
        this(
            product.getId(),
            product.getTitle(),
            product.getSlug(),
            product.getBarcode(),
            product.getSku(),
            product.getDescription(),
            product.getPrice(),
            product.getSalePrice(),
            product.getCostPrice(),
            product.getRating(),
            product.getRatingCount(),
            product.getActive(),
            product.getFeatured(),
            product.getCategoryId(),
            product.getBrandId(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}