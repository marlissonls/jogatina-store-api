package br.com.jogatinastore.catalog.category.presentation.http;

import br.com.jogatinastore.catalog.category.application.dto.CategoryPublicDTO;
import br.com.jogatinastore.catalog.category.application.dto.CategoryRequestDTO;
import br.com.jogatinastore.catalog.category.application.dto.CategoryResponseDTO;
import br.com.jogatinastore.catalog.category.application.service.CategoryService;
import br.com.jogatinastore.catalog.category.presentation.docs.CategoryControllerDocs;
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
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Endpoints for Categories management")
public class CategoryController implements CategoryControllerDocs {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // PUBLIC ENDPOINTS
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryPublicDTO>> findPublicCategories() {
        return ResponseEntity.ok().body(service.findPublicCategories());
    }

    @Override
    @GetMapping(path = "/slug/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryPublicDTO> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok().body(service.findBySlug(slug));
    }

    // MANAGER ENDPOINTS
    @Override
    @GetMapping(path = "/manager", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @Override
    @GetMapping(path = "/manager/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Override
    @PostMapping(path = "/manager", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody @Valid CategoryRequestDTO dto) {
        CategoryResponseDTO response = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    @PutMapping(path = "/manager/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid CategoryRequestDTO dto) {
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
