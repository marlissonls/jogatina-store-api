package br.com.springecommerce.catalog.category.application.dto;

import br.com.springecommerce.catalog.category.domain.model.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponseDto(
    UUID id,
    String title,
    String slug,
    String description,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public CategoryResponseDto(Category category) {
        this(
            category.getId(),
            category.getTitle(),
            category.getSlug(),
            category.getDescription(),
            category.getActive(),
            category.getCreatedAt(),
            category.getUpdatedAt()
        );
    }
}