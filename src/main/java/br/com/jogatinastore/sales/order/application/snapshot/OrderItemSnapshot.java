package br.com.jogatinastore.sales.order.application.snapshot;

import br.com.jogatinastore.sales.cart.application.snapshot.CartItemSnapshot;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemSnapshot(
    UUID productId,
    String productTitle,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal totalPrice
) {
    public OrderItemSnapshot(CartItemSnapshot item) {
        this(
                item.getProductId(),
                item.getProductTitle(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }

    public OrderItemSnapshot(OrderItemProjection item) {
        this(
                item.getProductId(),
                item.getProductTitle(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }
}
