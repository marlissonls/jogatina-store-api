package br.com.jogatinastore.sales.checkout.application.service;

import br.com.jogatinastore.catalog.product.application.service.ProductService;
import br.com.jogatinastore.catalog.product.domain.exception.ProductErrors;
import br.com.jogatinastore.catalog.product.domain.exception.ProductUnavailableException;
import br.com.jogatinastore.inventory.stock.application.movement.StockMovementItem;
import br.com.jogatinastore.inventory.stock.application.service.StockCommandService;
import br.com.jogatinastore.inventory.stock.domain.exception.InsufficientStockException;
import br.com.jogatinastore.inventory.stock.domain.exception.StockErrors;
import br.com.jogatinastore.sales.cart.application.service.CartService;
import br.com.jogatinastore.sales.cart.application.snapshot.CartItemSnapshot;
import br.com.jogatinastore.sales.cart.application.snapshot.CartSnapshot;
import br.com.jogatinastore.sales.cart.domain.exception.CartErrors;
import br.com.jogatinastore.sales.cart.domain.exception.CartIsEmptyException;
import br.com.jogatinastore.sales.cart.domain.model.Cart;
import br.com.jogatinastore.sales.checkout.application.dto.CheckoutResponseDto;
import br.com.jogatinastore.sales.order.application.contract.OrderCreationData;
import br.com.jogatinastore.sales.order.application.contract.OrderItemData;
import br.com.jogatinastore.sales.order.application.service.OrderService;
import br.com.jogatinastore.sales.order.application.snapshot.OrderItemSnapshot;
import br.com.jogatinastore.sales.order.domain.model.Order;
import br.com.jogatinastore.shared.exception.base.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private StockCommandService stockService;

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        checkoutService = new CheckoutService(
                productService,
                stockService,
                cartService,
                orderService
        );
    }

    @Test
    void shouldCreateOrderWhenCheckoutIsValid() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

        Cart cart = new Cart(userId);
        cart.addItem(productId, unitPrice, quantity);

        CartItemSnapshot item = mock(CartItemSnapshot.class);
        when(item.getProductId()).thenReturn(productId);
        when(item.getUnitPrice()).thenReturn(unitPrice);
        when(item.getQuantity()).thenReturn(quantity);

        CartSnapshot snapshot = new CartSnapshot(cart, List.of(item));

        Order order = new Order(userId, subtotal);

        OrderCreationData expectedOrderData = new OrderCreationData(
                userId,
                subtotal,
                List.of(new OrderItemData(productId, unitPrice, quantity))
        );

        when(cartService.getCartSnapshot(userId))
                .thenReturn(snapshot);

        when(orderService.create(expectedOrderData))
                .thenReturn(order);

        // When
        CheckoutResponseDto response = checkoutService.checkout(userId);

        // Then
        assertEquals(order.getId(), response.id());
        assertEquals(order.getCustomerId(), response.customerId());
        assertEquals(order.getSubtotalAmount(), response.subTotalAmount());
        assertEquals(
                List.of(new OrderItemSnapshot(item)),
                response.items()
        );

        verify(cartService).getCartSnapshot(userId);
        verify(productService).checkProductsAreActive(
                List.of(productId)
        );
        verify(stockService).reserveItems(
                List.of(new StockMovementItem(productId, quantity))
        );
        verify(orderService).create(expectedOrderData);
        verify(cartService).markAsConverted(cart);
    }

    @Test
    void shouldFailCheckoutWhenCartOpenCartIsNotFound() {
        // Given
        UUID userId = UUID.randomUUID();

        ResourceNotFoundException exception = new ResourceNotFoundException(
                CartErrors.Target.CART,
                CartErrors.Code.CART_NOT_FOUND
        );

        when(cartService.getCartSnapshot(userId))
                .thenThrow(exception);

        // When / Then
        assertThrows(
                ResourceNotFoundException.class,
                () -> checkoutService.checkout(userId)
        );

        verify(cartService).getCartSnapshot(userId);
        verifyNoInteractions(stockService, orderService);
    }

    @Test
    void shouldFailCheckoutWhenCartIsEmpty() {
        // Given
        UUID userId = UUID.randomUUID();

        CartIsEmptyException exception = new CartIsEmptyException(
                CartErrors.Target.CART,
                CartErrors.Code.CART_IS_EMPTY
        );

        when(cartService.getCartSnapshot(userId))
                .thenThrow(exception);

        // When / Then
        assertThrows(
                CartIsEmptyException.class,
                () -> checkoutService.checkout(userId)
        );

        verify(cartService).getCartSnapshot(userId);
        verifyNoInteractions(productService, stockService, orderService);
    }

    @Test
    void shouldFailCheckoutWhenProductIsNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;

        Cart cart = new Cart(userId);
        cart.addItem(productId, unitPrice, quantity);

        CartItemSnapshot item = mock(CartItemSnapshot.class);
        when(item.getProductId()).thenReturn(productId);

        CartSnapshot snapshot = new CartSnapshot(cart, List.of(item));

        ResourceNotFoundException exception = new ResourceNotFoundException(
                ProductErrors.Target.PRODUCT,
                ProductErrors.Code.PRODUCT_NOT_FOUND
        );

        List<UUID> productIds = snapshot.items()
                .stream()
                .map(CartItemSnapshot::getProductId)
                .toList();

        when(cartService.getCartSnapshot(userId))
                .thenReturn(snapshot);

        doThrow(exception)
                .when(productService)
                .checkProductsAreActive(productIds);

        // When / Then
        assertThrows(
                ResourceNotFoundException.class,
                () -> checkoutService.checkout(userId)
        );

        verify(cartService).getCartSnapshot(userId);
        verifyNoInteractions(stockService, orderService);
    }

    @Test
    void shouldFailCheckoutWhenProductIsUnavailable() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;

        Cart cart = new Cart(userId);
        cart.addItem(productId, unitPrice, quantity);

        CartItemSnapshot item = mock(CartItemSnapshot.class);
        when(item.getProductId()).thenReturn(productId);

        CartSnapshot snapshot = new CartSnapshot(cart, List.of(item));

        ProductUnavailableException exception = new ProductUnavailableException(
                ProductErrors.Target.PRODUCT,
                ProductErrors.Code.PRODUCT_UNAVAILABLE
        );

        List<UUID> productIds = snapshot.items()
                .stream()
                .map(CartItemSnapshot::getProductId)
                .toList();

        when(cartService.getCartSnapshot(userId))
                .thenReturn(snapshot);

        doThrow(exception)
                .when(productService)
                .checkProductsAreActive(productIds);

        // When / Then
        assertThrows(
                ProductUnavailableException.class,
                () -> checkoutService.checkout(userId)
        );

        verify(cartService).getCartSnapshot(userId);
        verifyNoInteractions(orderService);
    }

    @Test
    void shouldFailCheckoutWhenStockIsNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;

        Cart cart = new Cart(userId);
        cart.addItem(productId, unitPrice, quantity);

        CartItemSnapshot item = mock(CartItemSnapshot.class);
        when(item.getProductId()).thenReturn(productId);
        when(item.getQuantity()).thenReturn(quantity);

        CartSnapshot snapshot = new CartSnapshot(cart, List.of(item));

        ResourceNotFoundException exception = new ResourceNotFoundException(
                StockErrors.Target.PRODUCT,
                StockErrors.Code.STOCK_NOT_FOUND
        );

        List<StockMovementItem> reservationItems = snapshot.items()
                .stream()
                .map(cartItem -> new StockMovementItem(
                        cartItem.getProductId(),
                        cartItem.getQuantity()
                ))
                .toList();

        when(cartService.getCartSnapshot(userId))
                .thenReturn(snapshot);

        doThrow(exception)
                .when(stockService)
                .reserveItems(reservationItems);

        // When / Then
        assertThrows(
                ResourceNotFoundException.class,
                () -> checkoutService.checkout(userId)
        );

        verify(cartService).getCartSnapshot(userId);
        verifyNoInteractions(orderService);
    }

    @Test
    void shouldFailCheckoutWhenStockIsInsufficient() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;

        Cart cart = new Cart(userId);
        cart.addItem(productId, unitPrice, quantity);

        CartItemSnapshot item = mock(CartItemSnapshot.class);
        when(item.getProductId()).thenReturn(productId);
        when(item.getQuantity()).thenReturn(quantity);

        CartSnapshot snapshot = new CartSnapshot(cart, List.of(item));

        InsufficientStockException exception = new InsufficientStockException(
                StockErrors.Target.QUANTITY,
                StockErrors.Code.STOCK_QUANTITY_INSUFFICIENT
        );

        List<StockMovementItem> reservationItems = snapshot.items()
                .stream()
                .map(cartItem -> new StockMovementItem(
                        cartItem.getProductId(),
                        cartItem.getQuantity()
                ))
                .toList();

        when(cartService.getCartSnapshot(userId))
                .thenReturn(snapshot);

        doThrow(exception)
                .when(stockService)
                .reserveItems(reservationItems);

        // When / Then
        assertThrows(
                InsufficientStockException.class,
                () -> checkoutService.checkout(userId)
        );

        verify(cartService).getCartSnapshot(userId);
        verifyNoInteractions(orderService);
    }
}