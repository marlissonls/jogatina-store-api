package br.com.jogatinastore.inventory.stock.presentation.filter;

import java.util.UUID;

public record StockManagerFilter(
        UUID product,
        //UUID warehouse,
        Boolean lowStock
) {}
