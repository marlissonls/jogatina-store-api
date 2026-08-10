package br.com.jogatinastore.domain.sales.order.snapshot;

import br.com.jogatinastore.domain.sales.cart.snapshot.CartItemSnapshot;

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
