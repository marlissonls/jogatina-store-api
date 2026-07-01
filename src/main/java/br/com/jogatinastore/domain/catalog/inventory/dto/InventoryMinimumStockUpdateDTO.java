package br.com.jogatinastore.domain.catalog.inventory.dto;

import br.com.jogatinastore.domain.catalog.inventory.exception.InventoryErrors;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InventoryMinimumStockUpdateDTO(
        @NotNull(message = InventoryErrors.Code.INVENTORY_MIN_QUANTITY_REQUIRED)
        @Positive(message = InventoryErrors.Code.INVENTORY_MIN_QUANTITY_INVALID)
        Integer newMinQuantity
) {}