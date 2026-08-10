package br.com.jogatinastore.domain.sales.checkout.dto;

import br.com.jogatinastore.domain.sales.cart.snapshot.CartItemSnapshot;
import br.com.jogatinastore.domain.sales.order.entity.Order;
import br.com.jogatinastore.domain.sales.order.snapshot.OrderItemSnapshot;
import br.com.jogatinastore.domain.sales.order.status.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record CheckoutResponseDTO(
        UUID id,
        UUID userId,
        BigDecimal subTotalAmount,
        BigDecimal discountAmount,
        BigDecimal shippingAmount,
        BigDecimal totalAmount,
        OrderStatus status,
        List<OrderItemSnapshot> items
) {
    public CheckoutResponseDTO(Order order, List<CartItemSnapshot> items) {
        this(
                order.getId(),
                order.getUserId(),
                order.getSubtotalAmount(),
                order.getDiscountAmount(),
                order.getShippingAmount(),
                order.getTotalAmount(),
                order.getStatus(),
                toOrderItemSnapshots(items)
        );
    }

    private static List<OrderItemSnapshot> toOrderItemSnapshots(List<CartItemSnapshot> items) {
        return items
                .stream()
                .map(OrderItemSnapshot::new)
                .toList();
    }
}