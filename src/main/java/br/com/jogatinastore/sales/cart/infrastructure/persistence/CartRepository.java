package br.com.jogatinastore.sales.cart.infrastructure.persistence;

import br.com.jogatinastore.sales.cart.domain.model.Cart;
import br.com.jogatinastore.sales.cart.domain.status.CartStatus;
import br.com.jogatinastore.sales.cart.application.snapshot.CartItemSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query("""
        SELECT c
        FROM Cart c
        WHERE c.customerId = :customerId
           AND c.status = :status
    """)
    Optional<Cart> findByCustomerIdAndStatus(UUID customerId, CartStatus status);

    @Query(value = """
        SELECT
            ci.product_id AS productId,
            p.title AS productTitle,
            ci.unit_price AS unitPrice,
            ci.quantity AS quantity,
            ci.unit_price * ci.quantity AS totalPrice
        FROM cart_items ci
        JOIN products p
            ON p.id = ci.product_id
        WHERE ci.cart_id = :cartId
          AND p.active = TRUE
    """, nativeQuery = true)
    List<CartItemSnapshot> findCartItems(UUID cartId);
}