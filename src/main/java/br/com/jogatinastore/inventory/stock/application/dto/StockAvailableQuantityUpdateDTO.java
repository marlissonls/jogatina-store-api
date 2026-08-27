package br.com.jogatinastore.inventory.stock.application.dto;

import br.com.jogatinastore.inventory.stock.domain.exception.StockErrors;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockAvailableQuantityUpdateDTO(
        @NotNull(message = StockErrors.Code.STOCK_AVAILABLE_QUANTITY_REQUIRED)
        @Positive(message = StockErrors.Code.STOCK_AVAILABLE_QUANTITY_INVALID)
        Integer amount
) {}

