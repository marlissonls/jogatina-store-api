package br.com.jogatinastore.domain.inventory.stock.filter;

import java.util.UUID;

public record StockManagerFilter(
        UUID product,
        //UUID warehouse,
        Boolean lowStock
) {}
