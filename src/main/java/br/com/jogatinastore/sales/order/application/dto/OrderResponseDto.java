package br.com.jogatinastore.sales.order.application.dto;

import br.com.jogatinastore.sales.order.domain.model.Order;
import br.com.jogatinastore.sales.order.application.snapshot.OrderItemSnapshot;
import br.com.jogatinastore.sales.order.domain.status.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "OrderResponseDto")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderResponseDto(
        UUID id,
        UUID customerId,
        BigDecimal subTotalAmount,
        BigDecimal discountAmount,
        BigDecimal shippingAmount,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemSnapshot> items
) {
    public OrderResponseDto(Order order, List<OrderItemSnapshot> items) {
        this(
                order.getId(),
                order.getCustomerId(),
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
