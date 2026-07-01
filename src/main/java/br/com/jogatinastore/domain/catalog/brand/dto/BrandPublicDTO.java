package br.com.jogatinastore.domain.catalog.brand.dto;

import br.com.jogatinastore.domain.catalog.brand.entity.Brand;

import java.time.LocalDateTime;
import java.util.UUID;

public record BrandPublicDTO(
    UUID id,
    String title,
    String slug,
    String description,
    LocalDateTime createdAt
) {
    public BrandPublicDTO(Brand brand) {
        this(
            brand.getId(),
            brand.getTitle(),
            brand.getSlug(),
            brand.getDescription(),
            brand.getCreatedAt()
        );
    }
}
