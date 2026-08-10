package br.com.jogatinastore.domain.sales.cart.snapshot;

import java.math.BigDecimal;
import java.util.UUID;

public interface CartItemSnapshot {
    UUID getProductId();
    String getProductTitle();
    BigDecimal getUnitPrice();
    Integer getQuantity();
    BigDecimal getTotalPrice();
}
