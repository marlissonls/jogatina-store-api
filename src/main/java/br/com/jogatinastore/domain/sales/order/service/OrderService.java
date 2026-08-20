package br.com.jogatinastore.domain.sales.order.service;

import br.com.jogatinastore.domain.customer.customer.service.CustomerService;
import br.com.jogatinastore.domain.inventory.stock.movement.StockMovementItem;
import br.com.jogatinastore.domain.inventory.stock.service.StockCommandService;
import br.com.jogatinastore.domain.sales.order.contract.OrderCreationData;
import br.com.jogatinastore.domain.sales.order.dto.OrderResponseDTO;
import br.com.jogatinastore.domain.sales.order.entity.Order;
import br.com.jogatinastore.domain.sales.order.exception.OrderErrors;
import br.com.jogatinastore.domain.sales.order.repository.OrderRepository;
import br.com.jogatinastore.domain.sales.order.snapshot.OrderItemSnapshot;
import br.com.jogatinastore.domain.sales.order.valueobject.OrderItem;
import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
import br.com.jogatinastore.shared.PageResponse;
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

    public OrderResponseDTO getOrder(UUID id, UUID userId) {
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

        return new OrderResponseDTO(order, items);
    }

    public PageResponse<OrderResponseDTO> getOrders(UUID userId, Pageable pageable) {
        logger.debug("Fetching orders for customerId={}", userId);

        Page<Order> page = repository.findAllByCustomerId(userId, pageable);

        List<OrderResponseDTO> orders = page
                .map(order -> new OrderResponseDTO(order, null))
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
