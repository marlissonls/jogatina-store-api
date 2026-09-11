package br.com.jogatinastore.sales.cart.application.snapshot;

import br.com.jogatinastore.sales.cart.infrastructure.persistence.projection.CartItemProjection;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemSnapshot(
        UUID productId,
        String productTitle,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice
) {
    public CartItemSnapshot(CartItemProjection item) {
        this(
                item.getProductId(),
                item.getProductTitle(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getTotalPrice()
        );
    }
}