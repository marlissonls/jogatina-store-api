package br.com.jogatinastore.domain.inventory.stock.dto;

import br.com.jogatinastore.domain.inventory.stock.exception.StockErrors;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockMinimumQuantityUpdateDTO(
        @NotNull(message = StockErrors.Code.STOCK_MINIMUM_QUANTITY_REQUIRED)
        @Positive(message = StockErrors.Code.STOCK_MINIMUM_QUANTITY_INVALID)
        Integer newMinimumQuantity
) {}