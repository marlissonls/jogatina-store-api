package br.com.jogatinastore.domain.inventory.stock.dto;

import br.com.jogatinastore.domain.inventory.stock.entity.Stock;

import java.util.UUID;

public record StockResponseDTO(
        UUID id,
        UUID productId,
        Integer availableQuantity,
        Integer reservedQuantity,
        Integer minimumQuantity
) {
    public StockResponseDTO(Stock stock) {
        this(
                stock.getId(),
                stock.getProductId(),
                stock.getAvailableQuantity(),
                stock.getReservedQuantity(),
                stock.getMinimumQuantity()
        );
    }
}