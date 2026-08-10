package br.com.jogatinastore.domain.sales.cart.service;

import br.com.jogatinastore.domain.catalog.product.service.ProductService;
import br.com.jogatinastore.domain.catalog.product.snapshot.ProductSnapshot;
import br.com.jogatinastore.domain.inventory.stock.exception.StockErrors;
import br.com.jogatinastore.domain.sales.cart.dto.CartAddProductRequestDTO;
import br.com.jogatinastore.domain.sales.cart.dto.CartResponseDTO;
import br.com.jogatinastore.domain.sales.cart.entity.Cart;
import br.com.jogatinastore.domain.sales.cart.exception.CartErrors;
import br.com.jogatinastore.domain.sales.cart.snapshot.CartItemSnapshot;
import br.com.jogatinastore.domain.sales.cart.repository.CartRepository;
import br.com.jogatinastore.domain.sales.cart.snapshot.CartSnapshot;
import br.com.jogatinastore.domain.sales.cart.status.CartStatus;
import br.com.jogatinastore.infra.exception.CartItemUnavailableException;
import br.com.jogatinastore.infra.exception.InsufficientStockException;

import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
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

    public CartService(CartRepository repository, ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    public CartResponseDTO getCart(UUID userId) {
        logger.debug("Fetching open cart for userId={}", userId);

        Optional<Cart> cartOpt =  repository.findByUserIdAndStatus(userId, CartStatus.ACTIVE);

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
        logger.debug("Adding product to cart. userId={}, productId={}, quantity={}",
                userId, dto.productId(), dto.quantity());

        ProductSnapshot product = productService.getAvailableProduct(dto.productId());

        if (dto.quantity() > product.stock()) {
            throw new InsufficientStockException(
                    StockErrors.Target.QUANTITY,
                    StockErrors.Code.STOCK_QUANTITY_INSUFFICIENT
            );
        }

        Cart cart = repository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> repository.save(Cart.createFrom(userId)));

        cart.addItem(
                product.id(),
                product.effectivePrice(),
                dto.quantity()
        );

        logger.info("Product added to cart successfully. cartId={}, userId={}, productId={}, quantity={}",
                cart.getId(), userId, dto.productId(), dto.quantity());
    }

    @Transactional
    public void removeProduct(UUID userId, UUID productId) {
        logger.debug("Removing product from cart. userId={}, productId={}",
                userId, productId);

        Cart cart = findOpenCartOrThrow(userId);

        cart.removeItem(productId);

        logger.info("Product removed from cart successfully. cartId={}, userId={}, productId={}",
                cart.getId(), userId, productId);
    }

    public CartSnapshot getCartSnapshot(UUID userId) {
        Cart cart = findOpenCartOrThrow(userId);

        List<CartItemSnapshot> items = repository.findCartItems(cart.getId());

        return new CartSnapshot(cart, items);
    }

    private Cart findOpenCartOrThrow(UUID userId) {
        return repository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
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