package br.com.jogatinastore.sales.checkout.presentation.http;

import br.com.jogatinastore.sales.checkout.application.service.CheckoutService;
import br.com.jogatinastore.sales.checkout.application.dto.CheckoutResponseDto;
import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import br.com.jogatinastore.sales.checkout.presentation.docs.CheckoutControllerDocs;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/checkout")
@Tag(name = "Checkout", description = "Endpoints for Checkout management")
public class CheckoutController implements CheckoutControllerDocs {
    private final CheckoutService service;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CheckoutResponseDto> checkout(
            @AuthenticationPrincipal AuthenticatedUser auth
    ) {
        return ResponseEntity.ok().body(service.checkout(auth.getId()));
    }
}
