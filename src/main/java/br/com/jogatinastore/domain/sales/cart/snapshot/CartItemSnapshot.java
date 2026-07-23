package br.com.jogatinastore.domain.sales.cart.snapshot;

import java.math.BigDecimal;
import java.util.UUID;

public interface CartItemSnapshot {
    UUID getProductId();

    String getTitle();

    BigDecimal getUnitPrice();

    Integer getQuantity();
}
