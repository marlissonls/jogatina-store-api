package br.com.springecommerce.catalog.product.presentation.filter;

import java.math.BigDecimal;
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
