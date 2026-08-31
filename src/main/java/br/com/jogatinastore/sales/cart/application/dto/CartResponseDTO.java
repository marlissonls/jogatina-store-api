package br.com.jogatinastore.sales.cart.application.dto;

import br.com.jogatinastore.sales.cart.domain.model.Cart;
import br.com.jogatinastore.sales.cart.application.snapshot.CartItemSnapshot;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(name = "CartResponseDTO")
public record CartResponseDTO(
        UUID id,
        UUID customerId,
        BigDecimal subTotalAmount,
        List<CartItemSnapshot> items
) {
    public CartResponseDTO(Cart cart, List<CartItemSnapshot> items) {
        this(
                cart.getId(),
                cart.getCustomerId(),
                cart.getSubtotalAmount(),
                items
        );
    }
}
