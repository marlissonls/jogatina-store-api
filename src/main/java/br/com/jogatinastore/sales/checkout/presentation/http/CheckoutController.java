package br.com.jogatinastore.sales.checkout.presentation.http;

import br.com.jogatinastore.sales.checkout.application.service.CheckoutService;
import br.com.jogatinastore.sales.checkout.application.dto.CheckoutResponseDTO;
import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/checkout/v1")
public class CheckoutController {
    private final CheckoutService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }

    @PostMapping(produces = JSON)
    public ResponseEntity<CheckoutResponseDTO> checkout(
            @AuthenticationPrincipal AuthenticatedUser auth
    ) {
        return ResponseEntity.ok().body(service.checkout(UUID.fromString(auth.getId())));
    }
}
