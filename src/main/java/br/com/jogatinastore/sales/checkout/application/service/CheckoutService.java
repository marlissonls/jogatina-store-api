package br.com.jogatinastore.sales.checkout.application.service;

import br.com.jogatinastore.inventory.stock.application.movement.StockMovementItem;
import br.com.jogatinastore.inventory.stock.application.service.StockCommandService;
import br.com.jogatinastore.sales.cart.application.service.CartService;
import br.com.jogatinastore.sales.cart.application.snapshot.CartSnapshot;
import br.com.jogatinastore.sales.checkout.application.dto.CheckoutResponseDTO;
import br.com.jogatinastore.sales.order.application.contract.OrderCreationData;
import br.com.jogatinastore.sales.order.application.contract.OrderItemData;
import br.com.jogatinastore.sales.order.domain.model.Order;
import br.com.jogatinastore.sales.order.application.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CheckoutService {
    private final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    private final StockCommandService stockService;
    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutService(
            StockCommandService stockService,
            CartService cartService,
            OrderService orderService
    ) {
        this.stockService = stockService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @Transactional
    public CheckoutResponseDTO checkout(UUID userId) {
        logger.debug("Checkout started. customerId={}", userId);

        CartSnapshot snapshot = cartService.getCartSnapshot(userId);

        cartService.validateForCheckout(snapshot);

        stockService.reserveItems(buildReservationItems(snapshot));

        Order order = orderService.create(buildOrderCreationData(snapshot));

        cartService.markAsConverted(snapshot.cart());

        logger.info(
                "Checkout completed. customerId={}, orderId={}, items={}, total={}",
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
                snapshot.cart().getCustomerId(),
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
