package br.com.jogatinastore.domain.catalog.category.dto;

import br.com.jogatinastore.domain.catalog.category.entity.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponseDTO(
    UUID id,
    String title,
    String slug,
    String description,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public CategoryResponseDTO(Category category) {
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