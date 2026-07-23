package br.com.jogatinastore.domain.sales.cart.entity;

import br.com.jogatinastore.domain.sales.cart.status.CartStatus;
import br.com.jogatinastore.domain.sales.cart.valueobject.CartItem;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @Column
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Valid
    @ElementCollection
    @CollectionTable(
            name = "cart_item",
            joinColumns = @JoinColumn(name = "cart_id")
    )
    private Set<CartItem> items = new HashSet<>();

    @PositiveOrZero
    @Column(name = "subtotal_amount", nullable = false)
    private BigDecimal subtotalAmount = BigDecimal.ZERO;

    @PositiveOrZero
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CartStatus status = CartStatus.OPEN;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Cart() {}

    public Cart(UUID userId) {
        this.userId = userId;
    }

    @PrePersist
    protected void onCreate() {
        if (id == null)
            id = UUID.randomUUID();

        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addItem(
            UUID productId,
            BigDecimal unitPrice,
            Integer quantity
    ) {
        CartItem existingItem = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(quantity);
            existingItem.setUnitPrice(unitPrice);

        } else {
            items.add(new CartItem(
                    productId,
                    unitPrice,
                    quantity
            ));
        }

        recalculateTotals();
    }

    public void removeItem(UUID productId) {

        items.removeIf(
                item -> item.getProductId().equals(productId)
        );

        recalculateTotals();
    }

    private void recalculateTotals() {

        subtotalAmount = items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        totalAmount = subtotalAmount;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Set<CartItem> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public BigDecimal getSubtotalAmount() {
        return subtotalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public CartStatus getStatus() {
        return status;
    }

    public void setStatus(CartStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart cart)) return false;
        return Objects.equals(this.getId(), cart.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hashCode(id) : 0;
    }
}
