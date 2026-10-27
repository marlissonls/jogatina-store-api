package br.com.springecommerce.inventory.stock.application.dto;

import br.com.springecommerce.inventory.stock.domain.exception.StockErrors;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockMinimumQuantityUpdateDto(
        @NotNull(message = StockErrors.Code.STOCK_MINIMUM_QUANTITY_REQUIRED)
        @Positive(message = StockErrors.Code.STOCK_MINIMUM_QUANTITY_INVALID)
        Integer newMinimumQuantity
) {}