package br.com.jogatinastore.domain.sales.cart.dto;

import br.com.jogatinastore.domain.sales.cart.entity.Cart;
import br.com.jogatinastore.domain.sales.cart.snapshot.CartItemSnapshot;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponseDTO(
        UUID id,
        UUID userId,
        BigDecimal subTotalAmount,
        BigDecimal totalAmount,
        List<CartItemSnapshot> items
) {
    public CartResponseDTO(Cart cart, List<CartItemSnapshot> items) {
        this(
                cart.getId(),
                cart.getUserId(),
                cart.getSubtotalAmount(),
                cart.getTotalAmount(),
                items
        );
    }
}
