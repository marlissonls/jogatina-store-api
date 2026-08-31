package br.com.jogatinastore.sales.cart.presentation.docs;

import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import br.com.jogatinastore.sales.cart.application.dto.CartAddProductRequestDto;
import br.com.jogatinastore.sales.cart.application.dto.CartResponseDto;
import br.com.jogatinastore.shared.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface CartControllerDocs {

    @Operation(
            summary = "Find current user's cart",
            description = "Retrieves the shopping cart associated with the currently authenticated user.",
            tags = {"Carts"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<CartResponseDto> getCart(
            @AuthenticationPrincipal AuthenticatedUser auth
    );


    @Operation(
            summary = "Add product to cart",
            description = "Adds the specified product and quantity to the shopping cart of the currently authenticated user.",
            tags = {"Carts"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> addProduct(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @RequestBody @Valid CartAddProductRequestDto dto
    );


    @Operation(
            summary = "Remove product from cart",
            description = "Removes the specified product from the shopping cart of the currently authenticated user.",
            tags = {"Carts"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> removeProduct(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PathVariable UUID productId
    );
}

