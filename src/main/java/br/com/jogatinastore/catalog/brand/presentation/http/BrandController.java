package br.com.jogatinastore.catalog.brand.presentation.http;

import br.com.jogatinastore.catalog.brand.application.dto.BrandPublicDTO;
import br.com.jogatinastore.catalog.brand.application.dto.BrandRequestDTO;
import br.com.jogatinastore.catalog.brand.application.dto.BrandResponseDTO;
import br.com.jogatinastore.catalog.brand.application.service.BrandService;
import br.com.jogatinastore.catalog.brand.presentation.docs.BrandControllerDocs;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/brands")
@Tag(name = "Brands", description = "Endpoints for Brands management")
public class BrandController implements BrandControllerDocs {

    private final BrandService service;

    public BrandController(BrandService service) {
        this.service = service;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BrandPublicDTO>> findPublicBrands() {
        return ResponseEntity.ok().body(service.findPublicCategories());
    }

    @Override
    @GetMapping(path = "/slug/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BrandPublicDTO> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok().body(service.findBySlug(slug));
    }

    @Override
    @GetMapping(path = "/manager/slug/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<BrandResponseDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @Override
    @PostMapping(path = "/manager", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BrandResponseDTO> create(@RequestBody @Valid BrandRequestDTO dto) {
        BrandResponseDTO response = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    @GetMapping(path = "/manager/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BrandResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Override
    @PutMapping(path = "/manager/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BrandResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid BrandRequestDTO dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @Override
    @PatchMapping(path = "/manager/{id}/deactivate")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {

        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(path = "/manager/{id}/activate")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {

        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}
