package br.com.jogatinastore.sales.cart.application.dto;

import br.com.jogatinastore.sales.cart.domain.model.Cart;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(name = "CartResponseDto")
public record CartResponseDto(
        UUID id,
        UUID customerId,
        BigDecimal subTotalAmount,
        List<CartItemResponseDto> items
) {
    public CartResponseDto(Cart cart, List<CartItemResponseDto> items) {
        this(
                cart.getId(),
                cart.getCustomerId(),
                cart.getSubtotalAmount(),
                items
        );
    }
}
