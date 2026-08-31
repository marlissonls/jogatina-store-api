package br.com.jogatinastore.customer.customer.presentation.http;

import br.com.jogatinastore.customer.customer.presentation.docs.CustomerControllerDocs;
import br.com.jogatinastore.customer.customer.application.dto.CustomerCreateDTO;
import br.com.jogatinastore.customer.customer.application.dto.CustomerResponseDTO;
import br.com.jogatinastore.customer.customer.application.dto.CustomerUpdateDTO;
import br.com.jogatinastore.customer.customer.application.service.CustomerService;
import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import br.com.jogatinastore.shared.pagination.PageResponse;
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
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "Endpoints for Customers management")
public class CustomerController implements CustomerControllerDocs {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PageResponse<CustomerResponseDTO>> findAll(
            @PageableDefault(size = 12, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @Override
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CustomerResponseDTO> findById(@PathVariable UUID id) {

        return ResponseEntity.ok().body(service.findById(id));
    }

    @Override
    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CustomerResponseDTO> me(@AuthenticationPrincipal AuthenticatedUser auth) {

        return ResponseEntity.ok().body(service.me(UUID.fromString(auth.getId())));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @Override
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal.getId()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CustomerResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid CustomerUpdateDTO dto)
    {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @Override
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal.id")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}