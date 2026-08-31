package br.com.jogatinastore.sales.checkout.presentation.docs;

import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import br.com.jogatinastore.sales.checkout.application.dto.CheckoutResponseDto;
import br.com.jogatinastore.shared.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface CheckoutControllerDocs {

    @Operation(
            summary = "Process checkout",
            description = "Processes the checkout for the currently authenticated user's cart, creating the order based on the products and quantities currently in the cart.",
            tags = {"Checkout"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<CheckoutResponseDto> checkout(
            @AuthenticationPrincipal AuthenticatedUser auth
    );
}
