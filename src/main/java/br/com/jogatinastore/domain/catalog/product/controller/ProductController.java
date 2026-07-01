package br.com.jogatinastore.domain.catalog.product.controller;

import br.com.jogatinastore.domain.catalog.product.dto.ProductRequestDTO;
import br.com.jogatinastore.domain.catalog.product.dto.ProductResponseDTO;
import br.com.jogatinastore.domain.catalog.product.service.ProductService;
import br.com.jogatinastore.shared.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/procucts/v1")
public class ProductController {

    private final ProductService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // find filtered public
    @GetMapping(path = "/public", produces = JSON)
    public ResponseEntity<PageResponse<ProductResponseDTO>> findPublicProducts(
            @RequestParam(required = false) String search,

            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) Boolean isNew,

            @RequestParam(required = false) UUID category,
            @RequestParam(required = false) UUID brand,

            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,

            @RequestParam(required = false) Boolean onSale,

            @PageableDefault(size = 12, sort = "name") Pageable pageable
    ) {
        return ResponseEntity.ok().body(
            service.getPublicProducts(
                search, featured, isNew, category, brand,
                minPrice, maxPrice, onSale, pageable
            )
        );
    }

    // find filtered management
    @GetMapping(produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<PageResponse<ProductResponseDTO>> findProducts(
            @RequestParam(required = false) String search,

            @RequestParam(required = false) Boolean active,

            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) Boolean isNew,

            @RequestParam(required = false) UUID category,
            @RequestParam(required = false) UUID brand,

            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,

            @RequestParam(required = false) BigDecimal minCost,
            @RequestParam(required = false) BigDecimal maxCost,

            @RequestParam(required = false) Boolean availableForSale,
            @RequestParam(required = false) Boolean hasInventory,
            @RequestParam(required = false) Boolean onSale,

            @RequestParam(required = false) LocalDateTime createdFrom,
            @RequestParam(required = false) LocalDateTime createdTo,

            @RequestParam(required = false) LocalDateTime updatedFrom,
            @RequestParam(required = false) LocalDateTime updatedTo,

            @PageableDefault(size = 12, sort = "name") Pageable pageable
    ) {
        return ResponseEntity.ok().body(
            service.getProducts(
                search, active, featured, isNew, category, brand, minPrice, maxPrice,
                minCost, maxCost, availableForSale, hasInventory, onSale,
                createdFrom, createdTo, updatedFrom, updatedTo,
                pageable
            )
        );
    }

    // findbyId
    @GetMapping(path = "/{id}", produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    // create
    @PostMapping(consumes = JSON, produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO dto) {
        ProductResponseDTO response = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    // update
    @PutMapping(path = "/{id}", consumes = JSON, produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable @NotNull UUID id, @RequestBody @Valid ProductRequestDTO dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    // deactivate
    @PatchMapping(path = "/{id}/deactivate")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> deactivate(@PathVariable @NotNull UUID id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    // activate
    @PatchMapping(path = "/{id}/activate")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> activate(@PathVariable @NotNull UUID id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }

    // addImages

    // removeImage
}
