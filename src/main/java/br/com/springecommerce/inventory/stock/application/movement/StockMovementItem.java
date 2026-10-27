package br.com.springecommerce.inventory.stock.application.movement;

import java.util.UUID;

public record StockMovementItem(
        UUID productId,
        Integer quantity
) {}
