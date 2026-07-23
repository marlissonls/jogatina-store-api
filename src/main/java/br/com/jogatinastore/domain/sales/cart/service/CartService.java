package br.com.jogatinastore.domain.sales.cart.service;

import br.com.jogatinastore.domain.catalog.product.service.ProductService;
import br.com.jogatinastore.domain.catalog.product.snapshot.ProductSnapshot;
import br.com.jogatinastore.domain.inventory.stock.exception.StockErrors;
import br.com.jogatinastore.domain.sales.cart.dto.CartAddProductRequestDTO;
import br.com.jogatinastore.domain.sales.cart.dto.CartResponseDTO;
import br.com.jogatinastore.domain.sales.cart.entity.Cart;
import br.com.jogatinastore.domain.sales.cart.snapshot.CartItemSnapshot;
import br.com.jogatinastore.domain.sales.cart.repository.CartRepository;
import br.com.jogatinastore.domain.sales.cart.status.CartStatus;
import br.com.jogatinastore.infra.exception.InsufficientStockException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        Optional<Cart> cartOpt =  repository.findByUserIdAndStatus(userId, CartStatus.OPEN);

        Cart cart = cartOpt.orElseGet(() -> repository.save(new Cart(userId)));

        List<CartItemSnapshot> items = repository.findCartItems(cart.getId());

        logger.info("Cart retrieved successfully. cartId={}, userId={}, itemsCount={}",
                cart.getId(), userId, items.size());

        return new CartResponseDTO(cart, items);
    }

    @Transactional
    public void addProduct(UUID userId, CartAddProductRequestDTO dto) {
        logger.debug("Adding product to cart. userId={}, productId={}, quantity={}",
                userId, dto.productId(), dto.quantity());

        Cart cart = findOpenCartByUserAndStatus(userId, CartStatus.OPEN);

        ProductSnapshot product = productService.getAvailableProduct(dto.productId());

        if (dto.quantity() > product.stock()) {
            throw new InsufficientStockException(
                    StockErrors.Target.QUANTITY,
                    StockErrors.Code.STOCK_QUANTITY_INSUFFICIENT
            );
        }

        cart.addItem(
                product.id(),
                product.effectivePrice(),
                dto.quantity()
        );

        repository.save(cart);

        logger.info("Product added to cart successfully. cartId={}, userId={}, productId={}, quantity={}",
                cart.getId(), userId, dto.productId(), dto.quantity());
    }

    @Transactional
    public void removeProduct(UUID userId, UUID productId) {
        logger.debug("Removing product from cart. userId={}, productId={}",
                userId, productId);

        Cart cart = findOpenCartByUserAndStatus(userId, CartStatus.OPEN);

        cart.removeItem(productId);

        logger.info("Product removed from cart successfully. cartId={}, userId={}, productId={}",
                cart.getId(), userId, productId);
    }

    private Cart findOpenCartByUserAndStatus(UUID userId, CartStatus status) {

        Optional<Cart> cartOpt =  repository.findByUserIdAndStatus(userId, status);

        return cartOpt.orElseGet(() -> repository.save(new Cart(userId)));
    }
}