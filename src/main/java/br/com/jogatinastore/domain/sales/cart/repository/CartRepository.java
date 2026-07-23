package br.com.jogatinastore.domain.sales.cart.repository;

import br.com.jogatinastore.domain.sales.cart.entity.Cart;
import br.com.jogatinastore.domain.sales.cart.snapshot.CartItemSnapshot;
import br.com.jogatinastore.domain.sales.cart.status.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query("""
        SELECT c
        FROM Cart c
        WHERE c.userId = :userId
           AND c.status = :status
    """)
    Optional<Cart> findByUserIdAndStatus(UUID userId, CartStatus status);

    @Query(value = """
        SELECT
            ci.product_id AS productId,
            p.title AS title,
            ci.unit_price AS unitPrice,
            ci.quantity AS quantity
        FROM cart_item ci
        JOIN products p
            ON p.id = ci.product_id
        WHERE ci.cart_id = :cartId
          AND p.active = TRUE
    """, nativeQuery = true)
    List<CartItemSnapshot> findCartItems(UUID cartId);
}