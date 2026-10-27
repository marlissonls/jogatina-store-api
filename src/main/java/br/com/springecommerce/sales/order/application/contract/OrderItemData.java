package br.com.springecommerce.sales.order.application.contract;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemData(
        UUID productId,
        BigDecimal unitPrice,
        Integer quantity
) {}
