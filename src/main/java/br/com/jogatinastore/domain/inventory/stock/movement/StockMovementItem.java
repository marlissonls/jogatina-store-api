package br.com.jogatinastore.domain.inventory.stock.movement;

import java.util.UUID;

public record StockMovementItem(
        UUID productId,
        Integer quantity
) {}
