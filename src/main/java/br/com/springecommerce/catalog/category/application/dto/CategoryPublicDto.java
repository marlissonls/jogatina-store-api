package br.com.springecommerce.catalog.category.application.dto;

import br.com.springecommerce.catalog.category.domain.model.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryPublicDto(
    UUID id,
    String title,
    String slug,
    String description,
    LocalDateTime createdAt
) {
    public CategoryPublicDto(Category category) {
        this(
            category.getId(),
            category.getTitle(),
            category.getSlug(),
            category.getDescription(),
            category.getCreatedAt()
        );
    }
}