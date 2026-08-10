package br.com.jogatinastore.domain.sales.order.snapshot;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderItemProjection {
    UUID getProductId();
    String getProductTitle();
    Integer getQuantity();
    BigDecimal getUnitPrice();
    BigDecimal getTotalPrice();
}