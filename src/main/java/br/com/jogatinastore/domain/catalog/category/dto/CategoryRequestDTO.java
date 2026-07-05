package br.com.jogatinastore.domain.catalog.category.dto;

import br.com.jogatinastore.domain.catalog.category.exception.CategoryErrors;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
    @NotBlank(message = CategoryErrors.Code.CATEGORY_TITLE_REQUIRED)
    @Size(min = 3, max = 100, message = CategoryErrors.Code.CATEGORY_TITLE_SIZE)
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]*$", message = CategoryErrors.Code.CATEGORY_TITLE_INVALID)
    String title,

    @NotBlank(message = CategoryErrors.Code.CATEGORY_DESCRIPTION_REQUIRED)
    @Size(min = 3, max = 100, message = CategoryErrors.Code.CATEGORY_DESCRIPTION_SIZE)
    String description
) {}
