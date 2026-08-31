package br.com.jogatinastore.iam.role.presentation.docs;

import br.com.jogatinastore.iam.role.application.dto.RoleCreateDTO;
import br.com.jogatinastore.iam.role.application.dto.RoleResponseDTO;
import br.com.jogatinastore.iam.role.application.dto.RoleUpdateDTO;
import br.com.jogatinastore.shared.exception.response.ExceptionResponse;
import br.com.jogatinastore.shared.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public interface RoleControllerDocs {

    @Operation(
            summary = "Finding all Roles",
            description = "Finds all Roles with pagination",
            tags = {"Roles"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<PageResponse<RoleResponseDTO>> findAll(
            @PageableDefault(size = 12, sort = "title", direction = Sort.Direction.ASC)
            Pageable pageable
    );

    @Operation(
            summary = "Finding one Role by ID",
            description = "Finds a specific Role based on the provided ID",
            tags = {"Roles"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<RoleResponseDTO> findById(@PathVariable UUID id);

    @Operation(
            summary = "Finding one Role by title",
            description = "Finds a specific Role based on the provided title",
            tags = {"Roles"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<RoleResponseDTO> findByTitle(@RequestParam String title);

    @Operation(
            summary = "Add a new Role",
            description = "Adds a new Role by passing a JSON representation of the Role.",
            tags = {"Roles"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<RoleResponseDTO> create(@RequestBody @Valid RoleCreateDTO dto);

    @Operation(
            summary = "Update one Role",
            description = "Updates a Role based on the provided ID and JSON representation of the Role.",
            tags = {"Roles"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<RoleResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid RoleUpdateDTO dto
    );
}