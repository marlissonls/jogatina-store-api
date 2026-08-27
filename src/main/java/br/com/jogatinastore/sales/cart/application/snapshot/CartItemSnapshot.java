package br.com.jogatinastore.sales.cart.application.snapshot;

import java.math.BigDecimal;
import java.util.UUID;

public interface CartItemSnapshot {
    UUID getProductId();
    String getProductTitle();
    BigDecimal getUnitPrice();
    Integer getQuantity();
    BigDecimal getTotalPrice();
}
