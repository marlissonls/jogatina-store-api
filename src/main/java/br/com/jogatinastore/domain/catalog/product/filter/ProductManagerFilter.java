package br.com.jogatinastore.domain.catalog.product.filter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductManagerFilter(
        String search,

        UUID category,
        UUID brand,

        Boolean onSale,
        Boolean featured,
        Boolean isNew,

        BigDecimal minPrice,
        BigDecimal maxPrice,

        BigDecimal minCost,
        BigDecimal maxCost,

        Boolean active,
        Boolean hasAvailableStock,

        LocalDateTime createdFrom,
        LocalDateTime createdTo,

        LocalDateTime updatedFrom,
        LocalDateTime updatedTo
) {}
