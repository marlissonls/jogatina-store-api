package br.com.jogatinastore.domain.catalog.category.dto;

import br.com.jogatinastore.domain.catalog.category.entity.Category;

import java.time.LocalDateTime;
import java.util.UUID;

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