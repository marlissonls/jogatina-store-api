package br.com.jogatinastore.sales.order.infrastructure.persistence.repository;

import br.com.jogatinastore.sales.order.domain.model.Order;
import br.com.jogatinastore.sales.order.infrastructure.persistence.projection.OrderItemProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
        SELECT o
        FROM Order o
        WHERE o.id = :id
          AND o.customerId = :customerId
    """)
    Optional<Order> findByIdAndCustomerId(
        @Param("id") UUID id,
        @Param("customerId") UUID customerId
    );

    Page<Order> findAllByCustomerId(UUID customerId, Pageable pageable);

    @Query(value = """
        SELECT
            oi.product_id AS productId,
            p.title AS productTitle,
            oi.unit_price AS unitPrice,
            oi.quantity AS quantity,
            oi.unit_price * oi.quantity AS totalPrice
        FROM order_items oi
        JOIN products p
            ON p.id = oi.product_id
        WHERE oi.order_id = :orderId
    """, nativeQuery = true)
    List<OrderItemProjection> findOrderItems(@Param("orderId") UUID orderId);
}
