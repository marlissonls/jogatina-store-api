package br.com.jogatinastore.domain.user.controller;

import br.com.jogatinastore.domain.user.docs.UserControllerDocs;
import br.com.jogatinastore.domain.user.dto.CreateUserDTO;
import br.com.jogatinastore.shared.PageResponse;
import br.com.jogatinastore.domain.user.dto.UpdateUserDTO;
import br.com.jogatinastore.domain.user.dto.UserResponseDTO;
import br.com.jogatinastore.domain.user.service.UserService;
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

    private final UserService services;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public UserController(UserService services) {
        this.services = services;
    }

    @GetMapping(produces = JSON)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<PageResponse<UserResponseDTO>> findAll(
            @PageableDefault(size = 12, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(services.findAll(pageable));
    }

    @GetMapping(path = "/{id}", produces = JSON)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public UserResponseDTO findById(@PathVariable UUID id) {

        return services.findById(id);
    }

    @PostMapping(consumes = JSON, produces = JSON)
    @Override
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid CreateUserDTO dto) {
        UserResponseDTO response = services.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(path = "/{id}", consumes = JSON, produces = JSON)
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal.id")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<UserResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid UpdateUserDTO dto) {

        return ResponseEntity.ok().body(services.update(id, dto));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal.id")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<?> delete(@PathVariable UUID id) {

        services.delete(id);
        return ResponseEntity.noContent().build();
    }
}