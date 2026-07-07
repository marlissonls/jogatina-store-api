package br.com.jogatinastore.domain.catalog.product.controller;

import br.com.jogatinastore.domain.catalog.product.dto.ProductPublicResponseDTO;
import br.com.jogatinastore.domain.catalog.product.dto.ProductCreateDTO;
import br.com.jogatinastore.domain.catalog.product.dto.ProductResponseDTO;
import br.com.jogatinastore.domain.catalog.product.dto.ProductUpdateDTO;
import br.com.jogatinastore.domain.catalog.product.filter.ProductManagerFilter;
import br.com.jogatinastore.domain.catalog.product.filter.ProductPublicFilter;
import br.com.jogatinastore.domain.catalog.product.service.ProductService;
import br.com.jogatinastore.shared.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/v1")
public class ProductController {

    private final ProductService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // PUBLIC ENDPOINTS
    // searchPublicViewProducts
    // ?search={search}&category={category}&brand={brand}&onSale={onSale}&featured={featured}&isNew={isNew}&minPrice={minPrice}&maxPrice={maxPrice}
    @GetMapping(path = "/public", produces = JSON)
    public ResponseEntity<PageResponse<ProductPublicResponseDTO>> searchPublicViewProducts(
            @ParameterObject @ModelAttribute ProductPublicFilter filter,
            @PageableDefault(size = 12, sort = "title") Pageable pageable
    ) {
        return ResponseEntity.ok().body(
            service.getPublicViewProducts(filter, pageable)
        );
    }

    // findBySlug
    @GetMapping(path = "/public/slug/{slug}", produces = JSON)
    public ResponseEntity<ProductPublicResponseDTO> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok().body(service.findBySlug(slug));
    }

    // PRIVATE ENDPOINTS
    // searchManagerViewProducts
    @GetMapping(produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<PageResponse<ProductResponseDTO>> searchManagerViewProducts(
            @ParameterObject @ModelAttribute ProductManagerFilter filter,
            @PageableDefault(size = 12, sort = "title") Pageable pageable
    ) {
        return ResponseEntity.ok().body(
            service.getManagerViewProducts(filter, pageable)
        );
    }

    // findById
    @GetMapping(path = "/{id}", produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    // findByBarcode
    @GetMapping(path = "/barcode/{barcode}", produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProductResponseDTO> findByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok().body(service.findByBarcode(barcode));
    }

    // findBySku
    @GetMapping(path = "/sku/{sku}", produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProductResponseDTO> findBySku(@PathVariable String sku) {
        return ResponseEntity.ok().body(service.findBySku(sku));
    }

    // create
    @PostMapping(consumes = JSON, produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductCreateDTO dto) {
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
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable @NotNull UUID id,
            @RequestBody @Valid ProductUpdateDTO dto) {
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
