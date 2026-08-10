package br.com.jogatinastore.domain.sales.order.repository;

import br.com.jogatinastore.domain.sales.order.entity.Order;
import br.com.jogatinastore.domain.sales.order.snapshot.OrderItemProjection;
import br.com.jogatinastore.domain.sales.order.snapshot.OrderItemSnapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
        SELECT o
        FROM Order o
        WHERE o.id = :id
          AND o.userId = :userId
    """)
    Optional<Order> findByIdAndUserId(UUID id, UUID userId);

    Page<Order> findAllByUserId(UUID userId, Pageable pageable);

    @Query(value = """
        SELECT
            oi.product_id AS productId,
            p.title AS productTitle,
            oi.unit_price AS unitPrice,
            oi.quantity AS quantity,
            oi.unit_price * oi.quantity AS totalPrice
        FROM order_item oi
        JOIN products p
            ON p.id = oi.product_id
        WHERE oi.order_id = :orderId
          AND p.active = TRUE
    """, nativeQuery = true)
    List<OrderItemProjection> findOrderItems(UUID orderId);
}
