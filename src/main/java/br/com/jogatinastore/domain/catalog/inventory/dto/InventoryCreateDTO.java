package br.com.jogatinastore.domain.catalog.inventory.dto;

import br.com.jogatinastore.domain.catalog.inventory.exception.InventoryErrors;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record InventoryCreateDTO(
        @NotNull(message = InventoryErrors.Code.INVENTORY_PRODUCT_REQUIRED)
        UUID productId,

        @NotNull(message = InventoryErrors.Code.INVENTORY_AVAILABLE_QUANTITY_REQUIRED)
        @Positive(message = InventoryErrors.Code.INVENTORY_AVAILABLE_QUANTITY_INVALID)
        Integer availableQuantity,

        @NotNull(message = InventoryErrors.Code.INVENTORY_MIN_QUANTITY_REQUIRED)
        @Positive(message = InventoryErrors.Code.INVENTORY_MIN_QUANTITY_INVALID)
        Integer minQuantity
) {}