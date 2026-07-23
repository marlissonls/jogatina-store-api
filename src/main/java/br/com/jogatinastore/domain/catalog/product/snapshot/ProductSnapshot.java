package br.com.jogatinastore.domain.catalog.product.snapshot;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSnapshot(
        UUID id,
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