package br.com.jogatinastore.domain.catalog.product.dto;

import br.com.jogatinastore.domain.catalog.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductPublicResponseDTO(
        UUID id,
        String title,
        String slug,
        String description,
        BigDecimal price,
        BigDecimal salePrice,
        Boolean featured,
        BigDecimal rating,
        Integer ratingCount,
        UUID categoryId,
        UUID brandId
) {
    public ProductPublicResponseDTO(Product product) {
        this(
            product.getId(),
            product.getTitle(),
            product.getSlug(),
            product.getDescription(),
            product.getPrice(),
            product.getSalePrice(),
            product.getFeatured(),
            product.getRating(),
            product.getRatingCount(),
            product.getCategoryId(),
            product.getBrandId()
        );
    }
}