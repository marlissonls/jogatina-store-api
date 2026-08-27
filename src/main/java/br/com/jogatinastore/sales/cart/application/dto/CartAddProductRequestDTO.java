package br.com.jogatinastore.sales.cart.application.dto;

import br.com.jogatinastore.sales.cart.domain.exception.CartErrors;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CartAddProductRequestDTO(
        @NotNull(message = CartErrors.Code.CART_ITEM_PRODUCT_ID_REQUIRED)
        UUID productId,

        @Positive(message = CartErrors.Code.CART_ITEM_QUANTITY_INVALID)
        Integer quantity
) {}
