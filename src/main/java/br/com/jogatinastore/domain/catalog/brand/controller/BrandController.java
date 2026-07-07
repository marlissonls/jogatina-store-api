package br.com.jogatinastore.domain.catalog.brand.controller;

import br.com.jogatinastore.domain.catalog.brand.dto.BrandPublicDTO;
import br.com.jogatinastore.domain.catalog.brand.dto.BrandRequestDTO;
import br.com.jogatinastore.domain.catalog.brand.dto.BrandResponseDTO;
import br.com.jogatinastore.domain.catalog.brand.service.BrandService;
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
@RequestMapping("/api/brands/v1")
public class BrandController {

    private final BrandService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public BrandController(BrandService service) {
        this.service = service;
    }

    @GetMapping(path = "/public", produces = JSON)
    public ResponseEntity<List<BrandPublicDTO>> findPublicCategories() {
        return ResponseEntity.ok().body(service.findPublicCategories());
    }

    @GetMapping(path = "/public/slug/{slug}", produces = JSON)
    public ResponseEntity<BrandPublicDTO> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok().body(service.findBySlug(slug));
    }

    @GetMapping(produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<BrandResponseDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @PostMapping(consumes = JSON, produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<BrandResponseDTO> create(@RequestBody @Valid BrandRequestDTO dto) {
        BrandResponseDTO response = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping(path = "/{id}", produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<BrandResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping(path = "/{id}", consumes = JSON, produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<BrandResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid BrandRequestDTO dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @PatchMapping(path = "/{id}/deactivate")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> deactivate(@PathVariable UUID id) {

        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/activate")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> activate(@PathVariable UUID id) {

        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}
