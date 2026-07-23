package br.com.jogatinastore.domain.sales.cart.controller;

import br.com.jogatinastore.domain.sales.cart.dto.CartAddProductRequestDTO;
import br.com.jogatinastore.domain.sales.cart.dto.CartResponseDTO;
import br.com.jogatinastore.domain.sales.cart.service.CartService;
import br.com.jogatinastore.infra.security.principal.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carts/v1")
public class CartController {
    private final CartService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<CartResponseDTO> getCart(
            @AuthenticationPrincipal AuthenticatedUser auth
    ) {
        return ResponseEntity.ok(service.getCart(UUID.fromString(auth.getId())));
    }

    @PostMapping(value = "/items", consumes = JSON)
    public ResponseEntity<Void> addProduct(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @RequestBody @Valid CartAddProductRequestDTO dto
    ) {
        service.addProduct(UUID.fromString(auth.getId()), dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/product/{productId}")
    public ResponseEntity<Void> removeProduct(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PathVariable UUID productId
    ) {
        service.removeProduct(UUID.fromString(auth.getId()), productId);

        return ResponseEntity.noContent().build();
    }
}
