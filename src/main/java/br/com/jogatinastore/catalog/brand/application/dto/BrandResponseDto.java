package br.com.jogatinastore.catalog.brand.application.dto;

import br.com.jogatinastore.catalog.brand.domain.model.Brand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "BrandResponseDto")
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