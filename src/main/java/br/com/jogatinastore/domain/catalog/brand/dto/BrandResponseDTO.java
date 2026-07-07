package br.com.jogatinastore.domain.catalog.brand.dto;

import br.com.jogatinastore.domain.catalog.brand.entity.Brand;

import java.time.LocalDateTime;
import java.util.UUID;

public record BrandResponseDTO(
    UUID id,
    String title,
    String slug,
    String description,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public BrandResponseDTO(Brand brand) {
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