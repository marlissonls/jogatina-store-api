package br.com.jogatinastore.domain.sales.order.contract;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemData(
        UUID productId,
        BigDecimal unitPrice,
        Integer quantity
) {}
