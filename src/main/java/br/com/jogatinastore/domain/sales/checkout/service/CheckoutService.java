package br.com.jogatinastore.domain.sales.checkout.service;

import br.com.jogatinastore.domain.inventory.stock.movement.StockMovementItem;
import br.com.jogatinastore.domain.inventory.stock.service.StockCommandService;
import br.com.jogatinastore.domain.sales.cart.service.CartService;
import br.com.jogatinastore.domain.sales.cart.snapshot.CartSnapshot;
import br.com.jogatinastore.domain.sales.checkout.dto.CheckoutResponseDTO;
import br.com.jogatinastore.domain.sales.order.contract.OrderCreationData;
import br.com.jogatinastore.domain.sales.order.contract.OrderItemData;
import br.com.jogatinastore.domain.sales.order.entity.Order;
import br.com.jogatinastore.domain.sales.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CheckoutService {
    private final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    private final OrderService orderService;

    private final CartService cartService;
    private final StockCommandService stockService;

    public CheckoutService(
            OrderService orderService,
            CartService cartService,
            StockCommandService stockService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.stockService = stockService;
    }

    @Transactional
    public CheckoutResponseDTO checkout(UUID userId) {
        logger.debug("Checkout started. userId={}", userId);

        CartSnapshot snapshot = cartService.getCartSnapshot(userId);

        cartService.validateForCheckout(snapshot);

        stockService.reserveItems(buildReservationItems(snapshot));

        Order order = orderService.create(buildOrderCreationData(snapshot));

        cartService.markAsConverted(snapshot.cart());

        logger.info(
                "Checkout completed. userId={}, orderId={}, items={}, total={}",
                userId, order.getId(), snapshot.items().size(), order.getTotalAmount()
        );

        return new CheckoutResponseDTO(order, snapshot.items());
    }

    private List<StockMovementItem> buildReservationItems(CartSnapshot snapshot) {
        return snapshot.items()
                .stream()
                .map(item -> new StockMovementItem(
                        item.getProductId(),
                        item.getQuantity()
                ))
                .toList();
    }

    private OrderCreationData buildOrderCreationData(CartSnapshot snapshot) {
        return new OrderCreationData(
                snapshot.cart().getUserId(),
                snapshot.cart().getSubtotalAmount(),
                snapshot.items().stream()
                        .map(i -> new OrderItemData(
                                i.getProductId(),
                                i.getUnitPrice(),
                                i.getQuantity()
                        ))
                        .toList()
//                shipping.getAmount(),
//                coupon.getDiscountAmount()
        );
    }
}
