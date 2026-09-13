package br.com.jogatinastore.unittests.sales.cart.application.service;

import br.com.jogatinastore.catalog.product.application.service.ProductService;
import br.com.jogatinastore.catalog.product.application.snapshot.ProductSnapshot;
import br.com.jogatinastore.catalog.product.domain.exception.ProductErrors;
import br.com.jogatinastore.inventory.stock.domain.exception.InsufficientStockException;
import br.com.jogatinastore.sales.cart.application.dto.CartAddProductRequestDto;
import br.com.jogatinastore.sales.cart.application.dto.CartItemResponseDto;
import br.com.jogatinastore.sales.cart.application.dto.CartResponseDto;
import br.com.jogatinastore.sales.cart.application.service.CartService;
import br.com.jogatinastore.sales.cart.application.snapshot.CartItemSnapshot;
import br.com.jogatinastore.sales.cart.domain.exception.CartItemNotFoundException;
import br.com.jogatinastore.sales.cart.domain.model.Cart;
import br.com.jogatinastore.sales.cart.domain.status.CartStatus;
import br.com.jogatinastore.sales.cart.domain.valueobject.CartItem;
import br.com.jogatinastore.sales.cart.infrastructure.persistence.projection.CartItemProjection;
import br.com.jogatinastore.sales.cart.infrastructure.persistence.repository.CartRepository;
import br.com.jogatinastore.shared.exception.base.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository repository;

    @Mock
    private ProductService productService;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService(
                repository,
                productService
        );
    }

    @Test
    void shouldReturnTheExistingActiveCartWithItsItems() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Cart cart = Cart.createFrom(userId);

        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;

        cart.addItem(productId, unitPrice, quantity);

        CartItemProjection cartItemProjection = mock(CartItemProjection.class);

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));

        when(repository.findCartItems(cart.getId()))
                .thenReturn(List.of(cartItemProjection));

// When
        CartResponseDto response = cartService.getCart(userId);

// Then
        assertEquals(cart.getId(), response.id());
        assertEquals(userId, response.customerId());
        assertEquals(cart.getSubtotalAmount(), response.subTotalAmount());
        assertEquals(
                List.of(
                        new CartItemResponseDto(
                                new CartItemSnapshot(cartItemProjection)
                        )
                ),
                response.items()
        );

        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );
        verify(repository).findCartItems(cart.getId());
        verifyNoInteractions(productService);
    }

    @Test
    void shouldReturnAnInMemoryCartWhenNoActiveCartExists() {
        // Given
        UUID userId = UUID.randomUUID();

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.empty());

        // When
        CartResponseDto response = cartService.getCart(userId);

        // Then
        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(userId, response.customerId());
        assertEquals(BigDecimal.ZERO, response.subTotalAmount());
        assertTrue(response.items().isEmpty());

        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );
        verify(repository, never()).findCartItems(any());
    }

    @Test
    void shouldReturnAnInMemoryCartWhenOnlyAConvertedCartExists() {
        // Given
        UUID userId = UUID.randomUUID();

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.empty());

        // When
        CartResponseDto response = cartService.getCart(userId);

        // Then
        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(userId, response.customerId());
        assertEquals(BigDecimal.ZERO, response.subTotalAmount());
        assertTrue(response.items().isEmpty());

        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );
        verify(repository, never()).findCartItems(any());
    }


    @Test
    void shouldCreateAnActiveCartAndAddTheFirstProductWhenNoActiveCartExists() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;

        CartAddProductRequestDto dto =
                new CartAddProductRequestDto(productId, quantity);

        ProductSnapshot product = new ProductSnapshot(
                productId, true, unitPrice, null, 10
        );

        Cart savedCart = Cart.createFrom(userId);

        when(productService.getAvailableProduct(productId))
                .thenReturn(product);

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.empty());

        when(repository.save(any(Cart.class)))
                .thenReturn(savedCart);

        // When
        cartService.addProduct(userId, dto);

        // Then
        verify(productService).getAvailableProduct(productId);
        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );
        verify(repository).save(any(Cart.class));

        assertEquals(userId, savedCart.getCustomerId());
        assertEquals(
                unitPrice.multiply(BigDecimal.valueOf(quantity)),
                savedCart.getSubtotalAmount()
        );
    }


    @Test
    void shouldAddAProductToTheExistingActiveCart() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;

        CartAddProductRequestDto dto = new CartAddProductRequestDto(
                productId,
                quantity
        );

        ProductSnapshot product = new ProductSnapshot(
                productId, true, unitPrice, null, 10
        );

        Cart cart = Cart.createFrom(userId);

        when(productService.getAvailableProduct(productId))
                .thenReturn(product);

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));

        // When
        cartService.addProduct(userId, dto);

        // Then
        verify(productService).getAvailableProduct(productId);
        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );
        verify(repository, never()).save(any(Cart.class));

        assertEquals(
                unitPrice.multiply(BigDecimal.valueOf(quantity)),
                cart.getSubtotalAmount()
        );
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void shouldUpdateQuantityAndUnitPriceWhenTheProductIsAlreadyInTheCart() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        BigDecimal unitPrice = new BigDecimal("100.00");
        int existingQuantity = 2;
        int newQuantity = 3;

        CartAddProductRequestDto dto = new CartAddProductRequestDto(
                productId,
                newQuantity
        );

        ProductSnapshot product = new ProductSnapshot(
                productId,
                true,
                unitPrice,
                null,
                10
        );

        Cart cart = Cart.createFrom(userId);
        cart.addItem(productId, unitPrice, existingQuantity);

        CartItem existingItem = cart.getItems().iterator().next();

        when(productService.getAvailableProduct(productId))
                .thenReturn(product);

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));

        // When
        cartService.addProduct(userId, dto);

        // Then
        verify(productService).getAvailableProduct(productId);
        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );
        verify(repository, never()).save(any(Cart.class));

        assertEquals(1, cart.getItems().size());
        assertEquals(newQuantity, existingItem.getQuantity());
        assertEquals(unitPrice, existingItem.getUnitPrice());
        assertEquals(
                unitPrice.multiply(BigDecimal.valueOf(newQuantity)),
                cart.getSubtotalAmount()
        );
    }



    @Test
    void shouldFailToAddProductWhenRequestedQuantityExceedsAvailableStock() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        int requestedQuantity = 11;
        int availableStock = 10;

        CartAddProductRequestDto dto = new CartAddProductRequestDto(
                productId,
                requestedQuantity
        );

        ProductSnapshot product = new ProductSnapshot(
                productId,
                true,
                new BigDecimal("100.00"),
                null,
                availableStock
        );

        when(productService.getAvailableProduct(productId))
                .thenReturn(product);

        // When / Then
        assertThrows(
                InsufficientStockException.class,
                () -> cartService.addProduct(userId, dto)
        );

        verify(productService).getAvailableProduct(productId);
        verify(repository, never()).findByCustomerIdAndStatus(any(), any());
        verify(repository, never()).save(any(Cart.class));
    }


    @Test
    void shouldThrowWhenProductIsNotAvailable() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        CartAddProductRequestDto dto = new CartAddProductRequestDto(
                productId,
                2
        );

        ResourceNotFoundException exception = new ResourceNotFoundException(
                ProductErrors.Target.PRODUCT,
                ProductErrors.Code.PRODUCT_NOT_FOUND
        );

        when(productService.getAvailableProduct(productId))
                .thenThrow(exception);

        // When / Then
        assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.addProduct(userId, dto)
        );

        verify(productService).getAvailableProduct(productId);
        verifyNoInteractions(repository);
    }


    @Test
    void shouldRemoveProductSuccessfully() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;

        Cart cart = Cart.createFrom(userId);
        cart.addItem(productId, unitPrice, quantity);

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));

        // When
        cartService.removeProduct(userId, productId);

        // Then
        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );

        assertTrue(cart.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getSubtotalAmount());
    }


    @Test
    void shouldThrowWhenRemoveProductAndItIsNotInCart() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Cart cart = Cart.createFrom(userId);

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));

        // When / Then
        assertThrows(
                CartItemNotFoundException.class,
                () -> cartService.removeProduct(userId, productId)
        );

        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );
    }


    @Test
    void shouldThrowWhenWhenRemoveProductAndActiveCartDoesNotExist() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.empty());

        // When / Then
        assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.removeProduct(userId, productId)
        );

        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );
    }


    @Test
    void shouldRemoveProductAndKeepOtherItems() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productToRemoveId = UUID.randomUUID();
        UUID otherProductId = UUID.randomUUID();

        BigDecimal firstUnitPrice = new BigDecimal("100.00");
        BigDecimal secondUnitPrice = new BigDecimal("50.00");

        Cart cart = Cart.createFrom(userId);
        cart.addItem(productToRemoveId, firstUnitPrice, 2);
        cart.addItem(otherProductId, secondUnitPrice, 3);

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));

        // When
        cartService.removeProduct(userId, productToRemoveId);

        // Then
        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );

        assertEquals(1, cart.getItems().size());
        assertTrue(
                cart.getItems().stream()
                        .anyMatch(item -> item.getProductId().equals(otherProductId))
        );
        assertEquals(
                secondUnitPrice.multiply(BigDecimal.valueOf(3)),
                cart.getSubtotalAmount()
        );
    }


    @Test
    void shouldRemoveLastProductAndResetSubtotal() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        BigDecimal unitPrice = new BigDecimal("100.00");
        int quantity = 2;

        Cart cart = Cart.createFrom(userId);
        cart.addItem(productId, unitPrice, quantity);

        when(repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));

        // When
        cartService.removeProduct(userId, productId);

        // Then
        verify(repository).findByCustomerIdAndStatus(
                userId,
                CartStatus.ACTIVE
        );

        assertTrue(cart.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getSubtotalAmount());
    }
}