package br.com.jogatinastore.domain.sales.cart.valueobject;

import br.com.jogatinastore.domain.sales.cart.exception.CartErrors;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class CartItem {

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Positive(message = CartErrors.Code.CART_ITEM_UNIT_PRICE_INVALID)
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Positive(message = CartErrors.Code.CART_ITEM_QUANTITY_INVALID)
    @Column(nullable = false)
    private Integer quantity;

    protected CartItem() {}

    public CartItem(
            UUID productId,
            BigDecimal unitPrice,
            Integer quantity
    ) {
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
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

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem other)) return false;
        return Objects.equals(productId, other.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}