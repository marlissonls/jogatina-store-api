package br.com.springecommerce.catalog.brand.application.dto;

import br.com.springecommerce.catalog.brand.domain.model.Brand;

import java.time.LocalDateTime;
import java.util.UUID;

public record BrandResponseDto(
    UUID id,
    String title,
    String slug,
    String description,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public BrandResponseDto(Brand brand) {
        this(
            brand.getId(),
            brand.getTitle(),
            brand.getSlug(),
            brand.getDescription(),
            brand.getActive(),
            brand.getCreatedAt(),
            brand.getUpdatedAt()
        );
    }
}