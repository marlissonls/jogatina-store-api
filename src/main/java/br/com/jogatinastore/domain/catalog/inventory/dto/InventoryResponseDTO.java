package br.com.jogatinastore.domain.catalog.inventory.dto;

import br.com.jogatinastore.domain.catalog.inventory.entity.Inventory;

import java.util.UUID;

public record InventoryResponseDTO(
        UUID id,
        UUID productId,
        Integer availableQuantity,
        Integer reservedQuantity,
        Integer minQuantity
) {
    public InventoryResponseDTO(Inventory inventory) {
        this(
                inventory.getId(),
                inventory.getProduct().getId(),
                inventory.getAvailableQuantity(),
                inventory.getReservedQuantity(),
                inventory.getMinQuantity()
        );
    }
}