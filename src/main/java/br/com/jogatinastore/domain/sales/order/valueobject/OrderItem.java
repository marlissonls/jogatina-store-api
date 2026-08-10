package br.com.jogatinastore.domain.sales.order.valueobject;

import br.com.jogatinastore.domain.sales.order.contract.OrderItemData;
import br.com.jogatinastore.domain.sales.order.exception.OrderErrors;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class OrderItem {

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Positive(message = OrderErrors.Code.ORDER_ITEM_UNIT_PRICE_INVALID)
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Positive(message = OrderErrors.Code.ORDER_ITEM_QUANTITY_INVALID)
    @Column(nullable = false)
    private Integer quantity;

    protected OrderItem() {}

    public OrderItem(
            UUID productId,
            BigDecimal unitPrice,
            Integer quantity
    ) {
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public static OrderItem createFrom(OrderItemData item) {
        return new OrderItem(
                item.productId(),
                item.unitPrice(),
                item.quantity()
        );
    }

    public BigDecimal getTotalPrice() {

        return unitPrice.multiply(
                BigDecimal.valueOf(quantity)
        );
    }

    public UUID getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem other)) return false;
        return Objects.equals(productId, other.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
