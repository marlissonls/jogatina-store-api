package br.com.jogatinastore.domain.iam.user.controller;

import br.com.jogatinastore.domain.iam.user.docs.UserControllerDocs;
import br.com.jogatinastore.domain.iam.user.dto.CreateUserDTO;
import br.com.jogatinastore.shared.PageResponse;
import br.com.jogatinastore.domain.iam.user.dto.UserResponseDTO;
import br.com.jogatinastore.domain.iam.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/v1")
@Tag(name = "Users", description = "Endpoints for Users management")
public class UserController implements UserControllerDocs {

    private final UserService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(produces = JSON)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<PageResponse<UserResponseDTO>> findAll(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping(path = "/{id}", produces = JSON)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {

        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping(consumes = JSON, produces = JSON)
    @Override
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid CreateUserDTO dto) {
        UserResponseDTO response = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal.getId()")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<?> delete(@PathVariable UUID id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<?> deactivate(@PathVariable UUID id) {

        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<?> activate(@PathVariable UUID id) {

        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}