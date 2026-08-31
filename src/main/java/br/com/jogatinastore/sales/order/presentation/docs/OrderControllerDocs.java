package br.com.jogatinastore.sales.order.presentation.docs;

import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import br.com.jogatinastore.sales.order.application.dto.OrderResponseDto;
import br.com.jogatinastore.shared.exception.response.ExceptionResponse;
import br.com.jogatinastore.shared.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface OrderControllerDocs {

    @Operation(
            summary = "Find one Order",
            description = "Retrieves a specific order belonging to the currently authenticated user based on the provided order ID.",
            tags = {"Orders"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
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
    ResponseEntity<OrderResponseDto> getOrder(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PathVariable UUID id
    );


    @Operation(
            summary = "Find all Orders",
            description = "Retrieves all orders belonging to the currently authenticated user with pagination, sorted by creation date in descending order by default.",
            tags = {"Orders"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<PageResponse<OrderResponseDto>> getOrders(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PageableDefault(
                    size = 12,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    );


    @Operation(
            summary = "Cancel an Order",
            description = "Cancels a specific order belonging to the currently authenticated user based on the provided order ID.",
            tags = {"Orders"},
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
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @PathVariable UUID id
    );
}
