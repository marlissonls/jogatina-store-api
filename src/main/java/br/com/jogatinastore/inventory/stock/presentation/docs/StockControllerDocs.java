package br.com.jogatinastore.inventory.stock.presentation.docs;

import br.com.jogatinastore.inventory.stock.application.dto.StockAvailableQuantityUpdateDto;
import br.com.jogatinastore.inventory.stock.application.dto.StockCreateDto;
import br.com.jogatinastore.inventory.stock.application.dto.StockMinimumQuantityUpdateDto;
import br.com.jogatinastore.inventory.stock.application.dto.StockResponseDto;
import br.com.jogatinastore.inventory.stock.presentation.filter.StockManagerFilter;
import br.com.jogatinastore.shared.exception.response.ExceptionResponse;
import br.com.jogatinastore.shared.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface StockControllerDocs {

    @Operation(
            summary = "Add a new Stock",
            description = "Creates a new stock record by providing its initial stock information.",
            tags = {"Stocks"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
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
    ResponseEntity<StockResponseDto> create(
            @RequestBody @Valid StockCreateDto dto
    );


    @Operation(
            summary = "Find all Stocks",
            description = "Retrieves all stock records with pagination and optional filtering. Results are sorted by available quantity by default.",
            tags = {"Stocks"},
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
    ResponseEntity<PageResponse<StockResponseDto>> findAll(
            @ParameterObject
            @ModelAttribute StockManagerFilter filter,
            @PageableDefault(size = 12, sort = "availableQuantity")
            Pageable pageable
    );


    @Operation(
            summary = "Find one Stock",
            description = "Retrieves a specific stock record based on the provided stock ID.",
            tags = {"Stocks"},
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
    ResponseEntity<StockResponseDto> findById(
            @PathVariable UUID id
    );


    @Operation(
            summary = "Update minimum stock quantity",
            description = "Updates the minimum quantity threshold defined for the specified stock. This value can be used to identify when the available stock reaches a critical level.",
            tags = {"Stocks"},
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
    ResponseEntity<Void> updateMinimumQuantity(
            @PathVariable UUID id,
            @RequestBody @Valid StockMinimumQuantityUpdateDto dto
    );


    @Operation(
            summary = "Increase available stock",
            description = "Increases the available quantity of the specified stock by the amount provided.",
            tags = {"Stocks"},
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
    ResponseEntity<Void> increase(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDto dto
    );


    @Operation(
            summary = "Write off stock",
            description = "Reduces the available quantity of the specified stock by writing off the amount provided. This operation is intended for stock losses, damages, or other quantities that are no longer available.",
            tags = {"Stocks"},
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
    ResponseEntity<Void> writeOff(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDto dto
    );


    @Operation(
            summary = "Reserve stock",
            description = "Reserves the specified quantity of stock, reducing the quantity available for new reservations or other operations.",
            tags = {"Stocks"},
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
    ResponseEntity<Void> reserve(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDto dto
    );


    @Operation(
            summary = "Release reserved stock",
            description = "Releases the specified quantity of previously reserved stock, making it available again.",
            tags = {"Stocks"},
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
    ResponseEntity<Void> release(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDto dto
    );
}

