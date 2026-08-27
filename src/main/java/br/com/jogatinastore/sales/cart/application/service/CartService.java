package br.com.jogatinastore.sales.cart.application.service;

import br.com.jogatinastore.catalog.product.application.service.ProductService;
import br.com.jogatinastore.catalog.product.application.snapshot.ProductSnapshot;
import br.com.jogatinastore.inventory.stock.domain.exception.StockErrors;
import br.com.jogatinastore.sales.cart.application.dto.CartAddProductRequestDTO;
import br.com.jogatinastore.sales.cart.application.dto.CartResponseDTO;
import br.com.jogatinastore.sales.cart.domain.model.Cart;
import br.com.jogatinastore.sales.cart.domain.exception.CartErrors;
import br.com.jogatinastore.sales.cart.application.snapshot.CartItemSnapshot;
import br.com.jogatinastore.sales.cart.infrastructure.persistence.CartRepository;
import br.com.jogatinastore.sales.cart.application.snapshot.CartSnapshot;
import br.com.jogatinastore.sales.cart.domain.status.CartStatus;
import br.com.jogatinastore.sales.cart.domain.exception.CartItemUnavailableException;
import br.com.jogatinastore.inventory.stock.domain.exception.InsufficientStockException;

import br.com.jogatinastore.shared.exception.base.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository repository;
    private final ProductService productService;

    public CartService(
            CartRepository repository,
            ProductService productService
    ) {
        this.repository = repository;
        this.productService = productService;
    }

    public CartResponseDTO getCart(UUID userId) {
        logger.debug("Fetching open cart for customerId={}", userId);

        Optional<Cart> cartOpt =  repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE);

        Cart cart;
        List<CartItemSnapshot> items;

        if (cartOpt.isEmpty()) {
            cart = Cart.createFrom(userId);
            items = List.of();
        } else {
            cart = cartOpt.get();
            items = repository.findCartItems(cart.getId());
        }

        return new CartResponseDTO(cart, items);
    }

    @Transactional
    public void addProduct(UUID userId, CartAddProductRequestDTO dto) {
        logger.debug("Adding product to cart. customerId={}, productId={}, quantity={}",
                userId, dto.productId(), dto.quantity());

        ProductSnapshot product = productService.getAvailableProduct(dto.productId());

        if (dto.quantity() > product.stock()) {
            throw new InsufficientStockException(
                    StockErrors.Target.QUANTITY,
                    StockErrors.Code.STOCK_QUANTITY_INSUFFICIENT
            );
        }

        Cart cart = repository.findByCustomerIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> repository.save(Cart.createFrom(userId)));

        cart.addItem(
                product.id(),
                product.effectivePrice(),
                dto.quantity()
        );

        logger.info("Product added to cart successfully. cartId={}, customerId={}, productId={}, quantity={}",
                cart.getId(), userId, dto.productId(), dto.quantity());
    }

    @Transactional
    public void removeProduct(UUID userId, UUID productId) {
        logger.debug("Removing product from cart. customerId={}, productId={}",
                userId, productId);

        Cart cart = findOpenCartOrThrow(userId);

        cart.removeItem(productId);

        logger.info("Product removed from cart successfully. cartId={}, customerId={}, productId={}",
                cart.getId(), userId, productId);
    }

    public CartSnapshot getCartSnapshot(UUID customerId) {
        Cart cart = findOpenCartOrThrow(customerId);

        List<CartItemSnapshot> items = repository.findCartItems(cart.getId());

        return new CartSnapshot(cart, items);
    }

    private Cart findOpenCartOrThrow(UUID customerId) {
        return repository.findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CartErrors.Target.CART,
                        CartErrors.Code.CART_NOT_FOUND
                ));
    }

    public void validateForCheckout(CartSnapshot snapshot) {

        List<UUID> productIds = snapshot.items().stream()
                .map(CartItemSnapshot::getProductId)
                .toList();

        Map<UUID, ProductSnapshot> products = productService
                .getProductsForAvailabilityCheck(productIds)
                .stream()
                .collect(Collectors.toMap(
                        ProductSnapshot::id,
                        Function.identity()
                ));

        for (CartItemSnapshot item : snapshot.items()) {
            ProductSnapshot product = products.get(item.getProductId());

            if (!product.active()) {
                throw new CartItemUnavailableException(
                        CartErrors.Target.ITEM,
                        CartErrors.Code.CART_ITEM_UNAVAILABLE
                );
            }

            if (product.stock() < item.getQuantity()) {
                throw new InsufficientStockException(
                        StockErrors.Target.QUANTITY,
                        StockErrors.Code.STOCK_QUANTITY_INSUFFICIENT
                );
            }
        }
    }

    public void markAsConverted(Cart cart) {
        cart.markAsConverted();
    }
}