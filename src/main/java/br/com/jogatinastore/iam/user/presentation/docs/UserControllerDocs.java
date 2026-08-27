package br.com.jogatinastore.iam.user.presentation.docs;

import br.com.jogatinastore.iam.user.application.dto.request.CreateEmployeeInput;
import br.com.jogatinastore.iam.user.application.dto.request.UpdateUserRoleInput;
import br.com.jogatinastore.shared.exception.response.ExceptionResponse;
import br.com.jogatinastore.shared.pagination.PageResponse;
import br.com.jogatinastore.iam.user.application.dto.request.CreateUserInput;
import br.com.jogatinastore.iam.user.application.dto.response.UserResponseOutput;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface UserControllerDocs {

    @Operation(
            summary = "Finding all Users",
            description = "Finds all Users with pagination",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<PageResponse<UserResponseOutput>> findAll(
            @PageableDefault(size = 12, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    );

    @Operation(
            summary = "Finding one User",
            description = "Finds a specific User based on the provided ID",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<UserResponseOutput> findById(@PathVariable UUID id);

    @PostMapping
    @Operation(
            summary = "Add a new User",
            description = "Adds a new User by passing a JSON representation of the User.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201"),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<UserResponseOutput> create(@RequestBody @Valid CreateUserInput dto);

    @PostMapping
    @Operation(
            summary = "Add a new User Employee",
            description = "Adds a new User Employee by passing a JSON representation of the User Employee.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201"),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<UserResponseOutput> createEmployee(@RequestBody @Valid CreateEmployeeInput dto);

    @Operation(
            summary = "Assign role to User",
            description = "Assigns a role to the specified User",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<Void> assignRoleToUser(@RequestBody @Valid UpdateUserRoleInput dto);

    @Operation(
            summary = "Remove role from User",
            description = "Removes a role from the specified User",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<Void> removeRoleFromUser(@RequestBody @Valid UpdateUserRoleInput dto);

    @Operation(
            summary = "Delete one User",
            description = "Deletes a User based on the provided ID",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<?> delete(@PathVariable UUID id);

    @Operation(
            summary = "Deactivate user",
            description = "Deactivates a user account explicitly by userId. This action is intended for administrative control.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<?> deactivate(@PathVariable UUID id);

    @Operation(
            summary = "Activate user",
            description = "Activates a user account explicitly by userId. This action is intended for administrative control.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<?> activate(@PathVariable UUID id);
}
