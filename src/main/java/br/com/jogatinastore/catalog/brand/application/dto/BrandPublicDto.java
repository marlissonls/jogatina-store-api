package br.com.jogatinastore.catalog.brand.application.dto;

import br.com.jogatinastore.catalog.brand.domain.model.Brand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "BrandPublicDto")
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
