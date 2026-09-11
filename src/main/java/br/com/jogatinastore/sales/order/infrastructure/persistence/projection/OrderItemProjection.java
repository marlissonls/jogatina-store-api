package br.com.jogatinastore.sales.order.infrastructure.persistence.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderItemProjection {
    UUID getProductId();
    String getProductTitle();
    Integer getQuantity();
    BigDecimal getUnitPrice();
    BigDecimal getTotalPrice();
}