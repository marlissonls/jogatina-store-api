package br.com.jogatinastore.catalog.brand.presentation.docs;

import br.com.jogatinastore.catalog.brand.application.dto.BrandPublicDto;
import br.com.jogatinastore.catalog.brand.application.dto.BrandRequestDto;
import br.com.jogatinastore.catalog.brand.application.dto.BrandResponseDto;
import br.com.jogatinastore.shared.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface BrandControllerDocs {

    @Operation(
            summary = "Fetching public Brands",
            description = "Finds all public Brands",
            tags = {"Brands"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    ResponseEntity<List<BrandPublicDto>> findPublicBrands();

    @Operation(
            summary = "Fetching Brand by slug",
            description = "Finds a Brand by its slug",
            tags = {"Brands"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<BrandPublicDto> findBySlug(@PathVariable String slug);

    @Operation(
            summary = "Fetching all Brands",
            description = "Finds all Brands with pagination",
            tags = {"Brands"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<List<BrandResponseDto>> findAll();

    @Operation(
            summary = "Creating a Brand",
            description = "Creates a new Brand from the provided JSON representation",
            tags = {"Brands"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<BrandResponseDto> create(@RequestBody @Valid BrandRequestDto dto);

    @Operation(
            summary = "Fetching Brand by ID",
            description = "Finds a Brand by its ID",
            tags = {"Brands"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<BrandResponseDto> findById(@PathVariable UUID id);

    @Operation(
            summary = "Updating a Brand",
            description = "Updates an existing Brand from the provided JSON representation",
            tags = {"Brands"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<BrandResponseDto> update(
            @PathVariable UUID id,
            @RequestBody @Valid BrandRequestDto dto);

    @Operation(
            summary = "Deactivating a Brand",
            description = "Deactivates a Brand by its ID",
            tags = {"Brands"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> deactivate(@PathVariable UUID id);

    @Operation(
            summary = "Activating a Brand",
            description = "Activates a Brand by its ID",
            tags = {"Brands"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> activate(@PathVariable UUID id);
}
