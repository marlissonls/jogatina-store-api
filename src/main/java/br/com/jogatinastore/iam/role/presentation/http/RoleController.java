package br.com.jogatinastore.iam.role.presentation.http;

import br.com.jogatinastore.iam.role.presentation.docs.RoleControllerDocs;
import br.com.jogatinastore.iam.role.application.dto.RoleCreateDTO;
import br.com.jogatinastore.iam.role.application.dto.RoleResponseDTO;
import br.com.jogatinastore.iam.role.application.dto.RoleUpdateDTO;
import br.com.jogatinastore.iam.role.application.service.RoleService;
import br.com.jogatinastore.shared.pagination.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/roles/v1")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Roles", description = "Endpoints for role management")
public class RoleController implements RoleControllerDocs {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PageResponse<RoleResponseDTO>> findAll(
            @PageableDefault(size = 12, sort = "title", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @Override
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RoleResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Override
    @GetMapping(value = "/title/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RoleResponseDTO> findByTitle(
            @PathVariable String title
    ) {
        return ResponseEntity.ok(service.findByTitle(title));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RoleResponseDTO> create(
            @Valid @RequestBody RoleCreateDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @Override
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RoleResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody RoleUpdateDTO dto
    ) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }
}
