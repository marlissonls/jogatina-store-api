package br.com.springecommerce.inventory.stock.application.dto;

import br.com.springecommerce.inventory.stock.domain.model.Stock;

import java.util.UUID;

public record StockResponseDto(
        UUID id,
        UUID productId,
        Integer availableQuantity,
        Integer reservedQuantity,
        Integer minimumQuantity
) {
    public StockResponseDto(Stock stock) {
        this(
                stock.getId(),
                stock.getProductId(),
                stock.getAvailableQuantity(),
                stock.getReservedQuantity(),
                stock.getMinimumQuantity()
        );
    }
}