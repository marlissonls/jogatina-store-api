package br.com.springecommerce.sales.cart.infrastructure.persistence.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface CartItemProjection {
    UUID getProductId();
    String getProductTitle();
    BigDecimal getUnitPrice();
    Integer getQuantity();
    BigDecimal getTotalPrice();
}
