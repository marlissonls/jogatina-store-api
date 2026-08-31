package br.com.jogatinastore.catalog.category.application.dto;

import br.com.jogatinastore.catalog.category.domain.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "CategoryResponseDto")
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