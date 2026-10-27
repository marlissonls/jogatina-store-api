package br.com.springecommerce.sales.cart.application.dto;

import br.com.springecommerce.sales.cart.application.snapshot.CartItemSnapshot;

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