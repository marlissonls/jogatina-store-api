package br.com.springecommerce.inventory.stock.presentation.filter;

import java.util.UUID;

public record StockManagerFilter(
        UUID product,
        //UUID warehouse,
        Boolean lowStock
) {}
