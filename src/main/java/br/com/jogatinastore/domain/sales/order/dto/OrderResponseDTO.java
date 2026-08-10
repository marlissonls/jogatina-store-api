package br.com.jogatinastore.domain.sales.order.dto;

import br.com.jogatinastore.domain.sales.order.entity.Order;
import br.com.jogatinastore.domain.sales.order.snapshot.OrderItemSnapshot;
import br.com.jogatinastore.domain.sales.order.status.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderResponseDTO(
        UUID id,
        UUID userId,
        BigDecimal subTotalAmount,
        BigDecimal discountAmount,
        BigDecimal shippingAmount,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemSnapshot> items
) {
    public OrderResponseDTO(Order order, List<OrderItemSnapshot> items) {
        this(
                order.getId(),
                order.getUserId(),
                order.getSubtotalAmount(),
                order.getDiscountAmount(),
                order.getShippingAmount(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                items
        );
    }
}
