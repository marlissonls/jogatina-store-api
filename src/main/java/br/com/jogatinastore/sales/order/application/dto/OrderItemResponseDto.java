package br.com.jogatinastore.sales.order.application.dto;

import br.com.jogatinastore.sales.cart.application.snapshot.CartItemSnapshot;
import br.com.jogatinastore.sales.order.application.snapshot.OrderItemSnapshot;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDto (
        UUID productId,
        String productTitle,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
    public OrderItemResponseDto(OrderItemSnapshot item) {
        this(
                item.productId(),
                item.productTitle(),
                item.quantity(),
                item.unitPrice(),
                item.totalPrice()
        );
    }

    public OrderItemResponseDto(CartItemSnapshot item) {
        this(
                item.productId(),
                item.productTitle(),
                item.quantity(),
                item.unitPrice(),
                item.totalPrice()
        );
    }
}