package br.com.jogatinastore.domain.sales.cart.dto;

import br.com.jogatinastore.domain.sales.cart.exception.CartErrors;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CartAddProductRequestDTO(
        @NotNull(message = CartErrors.Code.CART_ITEM_PRODUCT_ID_REQUIRED)
        UUID productId,

        //@Positive(message = CartErrors.Code.CART_PRODUCT_QUANTITY_INVALID)
        Integer quantity
) {}
