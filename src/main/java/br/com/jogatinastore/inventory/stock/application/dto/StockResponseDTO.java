package br.com.jogatinastore.inventory.stock.application.dto;

import br.com.jogatinastore.inventory.stock.domain.model.Stock;

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