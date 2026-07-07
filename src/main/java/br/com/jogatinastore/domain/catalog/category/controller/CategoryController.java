package br.com.jogatinastore.domain.catalog.category.controller;

import br.com.jogatinastore.domain.catalog.category.dto.CategoryPublicDTO;
import br.com.jogatinastore.domain.catalog.category.dto.CategoryRequestDTO;
import br.com.jogatinastore.domain.catalog.category.dto.CategoryResponseDTO;
import br.com.jogatinastore.domain.catalog.category.service.CategoryService;
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
@RequestMapping("/api/categories/v1")
public class CategoryController {

    private final CategoryService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // PUBLIC ENDPOINTS
    @GetMapping(path = "/public", produces = JSON)
    public ResponseEntity<List<CategoryPublicDTO>> findPublicCategories() {
        return ResponseEntity.ok().body(service.findPublicCategories());
    }

    @GetMapping(path = "/public/slug/{slug}", produces = JSON)
    public ResponseEntity<CategoryPublicDTO> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok().body(service.findBySlug(slug));
    }

    // MANAGER ENDPOINTS
    @GetMapping(produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping(path = "/{id}", produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping(consumes = JSON, produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody @Valid CategoryRequestDTO dto) {
        CategoryResponseDTO response = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(path = "/{id}", consumes = JSON, produces = JSON)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid CategoryRequestDTO dto) {
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
