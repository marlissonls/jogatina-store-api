package br.com.jogatinastore.sales.cart.presentation.http;

import br.com.jogatinastore.sales.cart.application.dto.CartAddProductRequestDto;
import br.com.jogatinastore.sales.cart.application.dto.CartResponseDto;
import br.com.jogatinastore.sales.cart.application.service.CartService;
import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Carts", description = "Endpoints for Carts management")
public class CartController implements br.com.jogatinastore.sales.cart.presentation.docs.CartControllerDocs {
    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponseDto> getCart(
            @AuthenticationPrincipal AuthenticatedUser auth
    ) {
        return ResponseEntity.ok(service.getCart(auth.getId()));
    }

    @Override
    @PostMapping(value = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addProduct(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @RequestBody @Valid CartAddProductRequestDto dto
    ) {
        service.addProduct(auth.getId(), dto);

        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/items/product/{productId}")
    public ResponseEntity<Void> removeProduct(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PathVariable UUID productId
    ) {
        service.removeProduct(auth.getId(), productId);

        return ResponseEntity.noContent().build();
    }
}
