package br.com.jogatinastore.sales.order.presentation.http;

import br.com.jogatinastore.sales.order.application.dto.OrderResponseDto;
import br.com.jogatinastore.sales.order.application.service.OrderService;
import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import br.com.jogatinastore.shared.pagination.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Orders", description = "Endpoints for Orders management")
public class OrderController implements br.com.jogatinastore.sales.order.presentation.docs.OrderControllerDocs {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @Override
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDto> getOrder(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok().body(service.getOrder(id, UUID.fromString(auth.getId())));
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<OrderResponseDto>> getOrders(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(service.getOrders(UUID.fromString(auth.getId()), pageable));
    }

    @Override
    @PutMapping(path = "/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PathVariable UUID id
    ) {
        service.cancel(id, UUID.fromString(auth.getId()));
        return ResponseEntity.noContent().build();
    }
}
