package br.com.jogatinastore.sales.order.application.service;

import br.com.jogatinastore.customer.customer.application.service.CustomerService;
import br.com.jogatinastore.inventory.stock.application.movement.StockMovementItem;
import br.com.jogatinastore.inventory.stock.application.service.StockCommandService;
import br.com.jogatinastore.sales.order.application.contract.OrderCreationData;
import br.com.jogatinastore.sales.order.application.dto.OrderResponseDto;
import br.com.jogatinastore.sales.order.domain.model.Order;
import br.com.jogatinastore.sales.order.domain.exception.OrderErrors;
import br.com.jogatinastore.sales.order.infrastructure.persistence.OrderRepository;
import br.com.jogatinastore.sales.order.application.snapshot.OrderItemSnapshot;
import br.com.jogatinastore.sales.order.domain.valueobject.OrderItem;
import br.com.jogatinastore.shared.exception.base.ResourceNotFoundException;
import br.com.jogatinastore.shared.pagination.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repository;

    private final StockCommandService stockService;

    public OrderService(
            OrderRepository repository,
            StockCommandService stockService,
            CustomerService customerService
    ) {
        this.repository = repository;
        this.stockService = stockService;
    }

    public OrderResponseDto getOrder(UUID id, UUID userId) {
        logger.debug("Fetching order for orderId={}, customerId={}", id, userId);

        Order order = repository.findByIdAndCustomerId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        OrderErrors.Target.ID,
                        OrderErrors.Code.ORDER_NOT_FOUND
        ));

        List<OrderItemSnapshot> items = repository.findOrderItems(order.getId())
                .stream()
                .map(OrderItemSnapshot::new)
                .toList();

        return new OrderResponseDto(order, items);
    }

    public PageResponse<OrderResponseDto> getOrders(UUID userId, Pageable pageable) {
        logger.debug("Fetching orders for customerId={}", userId);

        Page<Order> page = repository.findAllByCustomerId(userId, pageable);

        List<OrderResponseDto> orders = page
                .map(order -> new OrderResponseDto(order, null))
                .getContent();

        return new PageResponse<>(
                orders,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    public Order create(OrderCreationData data) {
        Order order = Order.createFrom(data);

        return repository.save(order);
    }

    @Transactional
    public void cancel(UUID id, UUID userId) {
        logger.debug("Cancelling order. orderId={}, customerId={}", id, userId);

        Order order = repository.findByIdAndCustomerId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        OrderErrors.Target.ID,
                        OrderErrors.Code.ORDER_NOT_FOUND
                ));

//        if (order.getStatus() == OrderStatus.PAID) { paymentService.refund(order.getId()); }

        order.markAsCancelled();

        stockService.releaseItems(buildReservationItems(order.getItems()));
    }

    private List<StockMovementItem> buildReservationItems(Set<OrderItem> items) {
        return items
                .stream()
                .map(item -> new StockMovementItem(
                        item.getProductId(),
                        item.getQuantity()
                ))
                .toList();
    }
}
