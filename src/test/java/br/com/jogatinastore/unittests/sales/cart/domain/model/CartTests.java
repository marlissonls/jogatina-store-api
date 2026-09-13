package br.com.jogatinastore.unittests.sales.cart.domain.model;

import br.com.jogatinastore.sales.cart.domain.exception.CartItemNotFoundException;
import br.com.jogatinastore.sales.cart.domain.model.Cart;
import br.com.jogatinastore.sales.cart.domain.status.CartStatus;
import br.com.jogatinastore.sales.cart.domain.valueobject.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CartTests {
    private UUID customerId;
    private Cart cart;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        cart = Cart.createFrom(customerId);
    }

    @Test
    void shouldCreateAnActiveCartWithEmptyItemsAndZeroSubtotal() {
        // Given
        UUID customerId = UUID.randomUUID();

        // When
        Cart cart = Cart.createFrom(customerId);

        // Then
        assertNotNull(cart.getId());
        assertEquals(customerId, cart.getCustomerId());
        assertTrue(cart.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getSubtotalAmount());
        assertEquals(CartStatus.ACTIVE, cart.getStatus());
    }

    @Test
    void shouldAddProductAndCalculateSubtotal() {
        // Given
        UUID productId = UUID.randomUUID();
        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;

        // When
        cart.addItem(productId, unitPrice, quantity);

        // Then
        assertEquals(1, cart.getItems().size());

        CartItem item = cart.getItems().iterator().next();

        assertEquals(productId, item.getProductId());
        assertEquals(unitPrice, item.getUnitPrice());
        assertEquals(quantity, item.getQuantity());
        assertEquals(
                new BigDecimal("200.00"),
                cart.getSubtotalAmount()
        );
    }

    @Test
    void shouldAddMultipleProductsAndCalculateSubtotal() {
        // Given
        UUID firstProductId = UUID.randomUUID();
        UUID secondProductId = UUID.randomUUID();

        BigDecimal firstUnitPrice = new BigDecimal("100.00");
        BigDecimal secondUnitPrice = new BigDecimal("50.00");

        // When
        cart.addItem(firstProductId, firstUnitPrice, 2);
        cart.addItem(secondProductId, secondUnitPrice, 3);

        // Then
        assertEquals(2, cart.getItems().size());
        assertEquals(
                new BigDecimal("350.00"),
                cart.getSubtotalAmount()
        );
    }

    @Test
    void shouldUpdateExistingProductQuantityAndPrice() {
        // Given
        UUID productId = UUID.randomUUID();

        BigDecimal oldUnitPrice = new BigDecimal("100.00");
        BigDecimal newUnitPrice = new BigDecimal("80.00");

        cart.addItem(productId, oldUnitPrice, 2);

        // When
        cart.addItem(productId, newUnitPrice, 3);

        // Then
        assertEquals(1, cart.getItems().size());

        CartItem item = cart.getItems().iterator().next();

        assertEquals(productId, item.getProductId());
        assertEquals(newUnitPrice, item.getUnitPrice());
        assertEquals(3, item.getQuantity());
        assertEquals(new BigDecimal("240.00"), cart.getSubtotalAmount());
    }

    @Test
    void shouldRemoveProductAndRecalculateSubtotal() {
        // Given
        UUID firstProductId = UUID.randomUUID();
        UUID secondProductId = UUID.randomUUID();

        cart.addItem(firstProductId, new BigDecimal("100.00"), 2);
        cart.addItem(secondProductId, new BigDecimal("50.00"), 3);

        // When
        cart.removeItem(firstProductId);

        // Then
        assertEquals(1, cart.getItems().size());
        assertEquals(
                secondProductId,
                cart.getItems().iterator().next().getProductId()
        );
        assertEquals(new BigDecimal("150.00"), cart.getSubtotalAmount());
    }

    @Test
    void shouldThrowWhenRemovingAProductThatIsNotInTheCart() {
        // Given
        UUID productId = UUID.randomUUID();

        // When / Then
        assertThrows(
                CartItemNotFoundException.class,
                () -> cart.removeItem(productId)
        );
    }

    @Test
    void shouldRemoveLastProductAndResetSubtotal() {
        // Given
        UUID productId = UUID.randomUUID();

        cart.addItem(productId, new BigDecimal("100.00"), 2);

        // When
        cart.removeItem(productId);

        // Then
        assertTrue(cart.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getSubtotalAmount());
    }

    @Test
    void shouldMarkCartAsConverted() {
        // Given
        assertEquals(CartStatus.ACTIVE, cart.getStatus());

        // When
        cart.markAsConverted();

        // Then
        assertEquals(CartStatus.CONVERTED, cart.getStatus());
    }
}
