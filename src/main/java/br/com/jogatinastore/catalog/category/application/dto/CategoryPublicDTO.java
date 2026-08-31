package br.com.jogatinastore.catalog.category.application.dto;

import br.com.jogatinastore.catalog.category.domain.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "CategoryPublicDTO")
public record CategoryPublicDTO(
    UUID id,
    String title,
    String slug,
    String description,
    LocalDateTime createdAt
) {
    public CategoryPublicDTO(Category category) {
        this(
            category.getId(),
            category.getTitle(),
            category.getSlug(),
            category.getDescription(),
            category.getCreatedAt()
        );
    }
}