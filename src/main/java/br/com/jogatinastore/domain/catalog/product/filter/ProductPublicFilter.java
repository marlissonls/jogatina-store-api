package br.com.jogatinastore.domain.catalog.product.filter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductPublicFilter(
        String search,

        UUID category,
        UUID brand,

        Boolean onSale,
        Boolean featured,
        Boolean isNew,

        BigDecimal minPrice,
        BigDecimal maxPrice
) {}
