package br.com.jogatinastore.sales.checkout.application.dto;

import br.com.jogatinastore.sales.cart.application.snapshot.CartItemSnapshot;
import br.com.jogatinastore.sales.order.domain.model.Order;
import br.com.jogatinastore.sales.order.application.snapshot.OrderItemSnapshot;
import br.com.jogatinastore.sales.order.domain.status.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record CheckoutResponseDTO(
        UUID id,
        UUID customerId,
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
                order.getCustomerId(),
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