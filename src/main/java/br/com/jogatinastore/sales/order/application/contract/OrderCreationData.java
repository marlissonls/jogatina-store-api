package br.com.jogatinastore.sales.order.application.contract;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderCreationData(
        UUID customerId,
        BigDecimal subtotalAmount,
        List<OrderItemData> items
) {}
