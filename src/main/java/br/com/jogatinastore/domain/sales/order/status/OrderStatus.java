package br.com.jogatinastore.domain.sales.order.status;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAYMENT_PROCESSING,
    PAID,
    PICKING,
    PACKING,
    READY_FOR_SHIPMENT,
    SHIPPED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    public boolean canBeCancelled() {
        return this == PENDING_PAYMENT;
    }
}