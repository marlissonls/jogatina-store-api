package br.com.jogatinastore.sales.order.presentation.http;

import br.com.jogatinastore.sales.order.application.dto.OrderResponseDTO;
import br.com.jogatinastore.sales.order.application.service.OrderService;
import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import br.com.jogatinastore.shared.pagination.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders/v1")
public class OrderController {
    private final OrderService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}", produces = JSON)
    public ResponseEntity<OrderResponseDTO> getOrder(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok().body(service.getOrder(id, UUID.fromString(auth.getId())));
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<PageResponse<OrderResponseDTO>> getOrders(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(service.getOrders(UUID.fromString(auth.getId()), pageable));
    }

    @PutMapping(path = "/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PathVariable UUID id
    ) {
        service.cancel(id, UUID.fromString(auth.getId()));
        return ResponseEntity.noContent().build();
    }
}
