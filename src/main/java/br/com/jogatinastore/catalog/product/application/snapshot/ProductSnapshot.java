package br.com.jogatinastore.catalog.product.application.snapshot;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSnapshot(
        UUID id,
        Boolean active,
        BigDecimal price,
        BigDecimal salePrice,
        Integer stock
) {
    public BigDecimal effectivePrice() {
        return salePrice != null && salePrice.compareTo(price) < 0
                ? salePrice
                : price;
    }
}