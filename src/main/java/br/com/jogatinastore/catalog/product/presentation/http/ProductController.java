package br.com.jogatinastore.catalog.product.presentation.http;

import br.com.jogatinastore.catalog.product.application.dto.*;
import br.com.jogatinastore.catalog.product.presentation.docs.ProductControllerDocs;
import br.com.jogatinastore.catalog.product.presentation.filter.ProductManagerFilter;
import br.com.jogatinastore.catalog.product.presentation.filter.ProductPublicFilter;
import br.com.jogatinastore.catalog.product.application.service.ProductService;
import br.com.jogatinastore.shared.pagination.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Endpoints for Products management")
public class ProductController implements ProductControllerDocs {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // PUBLIC ENDPOINTS
    // searchPublicViewProducts
    // ?search={search}&category={category}&brand={brand}&onSale={onSale}&featured={featured}&isNew={isNew}&minPrice={minPrice}&maxPrice={maxPrice}
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<ProductPublicResponseDTO>> searchPublicViewProducts(
            @ParameterObject @ModelAttribute ProductPublicFilter filter,
            @PageableDefault(size = 12, sort = "title") Pageable pageable
    ) {
        return ResponseEntity.ok().body(
            service.getPublicViewProducts(filter, pageable)
        );
    }

    // findBySlug
    @Override
    @GetMapping(path = "/slug/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductPublicResponseDTO> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok().body(service.findBySlug(slug));
    }

    // PRIVATE ENDPOINTS
    // searchManagerViewProducts
    @Override
    @GetMapping(path = "/manager", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PageResponse<ProductWithStockResponseDTO>> searchManagerViewProducts(
            @ParameterObject @ModelAttribute ProductManagerFilter filter,
            @PageableDefault(size = 12, sort = "title") Pageable pageable
    ) {
        return ResponseEntity.ok().body(
            service.getManagerViewProducts(filter, pageable)
        );
    }

    // findById
    @Override
    @GetMapping(path = "/manager/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProductWithStockResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    // findByBarcode
    @Override
    @GetMapping(path = "/manager/barcode/{barcode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProductWithStockResponseDTO> findByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok().body(service.findByBarcode(barcode));
    }

    // findBySku
    @Override
    @GetMapping(path = "/manager/sku/{sku}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProductWithStockResponseDTO> findBySku(@PathVariable String sku) {
        return ResponseEntity.ok().body(service.findBySku(sku));
    }

    // create
    @Override
    @PostMapping(path = "/manager", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
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
    @Override
    @PutMapping(path = "/manager/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable @NotNull UUID id,
            @RequestBody @Valid ProductUpdateDTO dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    // deactivate
    @Override
    @PatchMapping(path = "/manager/{id}/deactivate")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deactivate(@PathVariable @NotNull UUID id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    // activate
    @Override
    @PatchMapping(path = "/manager/{id}/activate")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> activate(@PathVariable @NotNull UUID id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }

    // addImages

    // removeImage
}
