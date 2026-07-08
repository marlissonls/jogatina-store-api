package br.com.jogatinastore.domain.inventory.stock.dto;

import br.com.jogatinastore.domain.inventory.stock.exception.StockErrors;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockAvailableQuantityUpdateDTO(
        @NotNull(message = StockErrors.Code.STOCK_AVAILABLE_QUANTITY_REQUIRED)
        @Positive(message = StockErrors.Code.STOCK_AVAILABLE_QUANTITY_INVALID)
        Integer amount
) {}

