package br.com.jogatinastore.catalog.brand.application.dto;

import br.com.jogatinastore.catalog.brand.domain.model.Brand;

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
