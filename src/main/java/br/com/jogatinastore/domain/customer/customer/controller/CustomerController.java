package br.com.jogatinastore.domain.customer.customer.controller;

import br.com.jogatinastore.domain.customer.customer.docs.CustomerControllerDocs;
import br.com.jogatinastore.domain.customer.customer.dto.CustomerCreateDTO;
import br.com.jogatinastore.domain.customer.customer.dto.CustomerResponseDTO;
import br.com.jogatinastore.domain.customer.customer.dto.CustomerUpdateDTO;
import br.com.jogatinastore.domain.customer.customer.service.CustomerService;
import br.com.jogatinastore.infra.security.principal.AuthenticatedUser;
import br.com.jogatinastore.shared.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers/v1")
@Tag(name = "Customers", description = "Endpoints for Customers management")
public class CustomerController implements CustomerControllerDocs {

    private final CustomerService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping(produces = JSON)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<PageResponse<CustomerResponseDTO>> findAll(
            @PageableDefault(size = 12, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping(path = "/{id}", produces = JSON)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<CustomerResponseDTO> findById(@PathVariable UUID id) {

        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping(path = "/me", produces = JSON)
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<CustomerResponseDTO> me(@AuthenticationPrincipal AuthenticatedUser auth) {

        return ResponseEntity.ok().body(service.me(UUID.fromString(auth.getId())));
    }

    @PostMapping(consumes = JSON, produces = JSON)
    @Override
    public ResponseEntity<CustomerResponseDTO> create(
            @AuthenticationPrincipal AuthenticatedUser auth,
            @RequestBody @Valid CustomerCreateDTO dto
    ) {
        CustomerResponseDTO response = service.create(UUID.fromString(auth.getId()), dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(path = "/{id}", consumes = JSON, produces = JSON)
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal.getId()")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<CustomerResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid CustomerUpdateDTO dto)
    {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal.id")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<?> delete(@PathVariable UUID id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}