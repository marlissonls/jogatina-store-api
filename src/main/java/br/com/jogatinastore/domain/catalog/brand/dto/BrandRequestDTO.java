package br.com.jogatinastore.domain.catalog.brand.dto;

import br.com.jogatinastore.domain.catalog.brand.exception.BrandErrors;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BrandRequestDTO(
    @NotBlank(message = BrandErrors.Code.BRAND_TITLE_REQUIRED)
    @Size(min = 3, max = 100, message = BrandErrors.Code.BRAND_TITLE_SIZE)
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]*$", message = BrandErrors.Code.BRAND_TITLE_INVALID)
    String title,

    @NotBlank(message = BrandErrors.Code.BRAND_DESCRIPTION_REQUIRED)
    @Size(min = 3, max = 100, message = BrandErrors.Code.BRAND_DESCRIPTION_SIZE)
    String description
) {}
