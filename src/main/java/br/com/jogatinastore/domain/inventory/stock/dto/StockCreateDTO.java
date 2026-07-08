package br.com.jogatinastore.domain.inventory.stock.dto;

import br.com.jogatinastore.domain.inventory.stock.exception.StockErrors;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record StockCreateDTO(
        @NotNull(message = StockErrors.Code.STOCK_PRODUCT_REQUIRED)
        UUID productId,

        @NotNull(message = StockErrors.Code.STOCK_AVAILABLE_QUANTITY_REQUIRED)
        @Positive(message = StockErrors.Code.STOCK_AVAILABLE_QUANTITY_INVALID)
        Integer availableQuantity,

        @NotNull(message = StockErrors.Code.STOCK_MINIMUM_QUANTITY_REQUIRED)
        @Positive(message = StockErrors.Code.STOCK_MINIMUM_QUANTITY_INVALID)
        Integer minimumQuantity
) {}