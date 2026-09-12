package br.com.jogatinastore.sales.cart.application.dto;

import br.com.jogatinastore.sales.cart.application.snapshot.CartItemSnapshot;
import br.com.jogatinastore.sales.cart.infrastructure.persistence.projection.CartItemProjection;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponseDto(
        UUID productId,
        String productTitle,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice
) {
    public CartItemResponseDto(CartItemSnapshot snapshot) {
        this(
                snapshot.productId(),
                snapshot.productTitle(),
                snapshot.unitPrice(),
                snapshot.quantity(),
                snapshot.totalPrice()
        );
    }
}