package br.com.springecommerce.catalog.brand.application.dto;

import br.com.springecommerce.catalog.brand.domain.model.Brand;

import java.time.LocalDateTime;
import java.util.UUID;

public record BrandPublicDto(
    UUID id,
    String title,
    String slug,
    String description,
    LocalDateTime createdAt
) {
    public BrandPublicDto(Brand brand) {
        this(
            brand.getId(),
            brand.getTitle(),
            brand.getSlug(),
            brand.getDescription(),
            brand.getCreatedAt()
        );
    }
}
