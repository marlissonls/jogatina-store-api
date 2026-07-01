package br.com.jogatinastore.domain.catalog.inventory.controller;

import br.com.jogatinastore.domain.catalog.inventory.dto.InventoryCreateDTO;
import br.com.jogatinastore.domain.catalog.inventory.dto.InventoryResponseDTO;
import br.com.jogatinastore.domain.catalog.inventory.dto.InventoryMinimumStockUpdateDTO;
import br.com.jogatinastore.domain.catalog.inventory.dto.InventoryAvailableQuantityUpdateDTO;
import br.com.jogatinastore.domain.catalog.inventory.service.InventoryService;
import br.com.jogatinastore.shared.PageResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/inventories/v1")
@PreAuthorize("hasRole('MANAGER')")
public class InventoryController {

    private final InventoryService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping(consumes = JSON, produces = JSON)
    public ResponseEntity<InventoryResponseDTO> create(@RequestBody @Valid InventoryCreateDTO dto) {
        var response = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<PageResponse<InventoryResponseDTO>> findAll(
            @RequestParam(required = false) UUID product,
            //@RequestParam(required = false) UUID warehouseId,
            @RequestParam(required = false) Boolean lowStock,
            @PageableDefault(size = 12, sort = "name") Pageable pageable
    ) {
        return ResponseEntity.ok().body(service.findAll(
                product, lowStock, pageable
        ));
    }

    @GetMapping(path = "/{id}", produces = JSON)
    public ResponseEntity<InventoryResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PatchMapping(path = "/{id}/minimum-stock", produces = JSON)
    public ResponseEntity<Void> updateMinQuantity(
            @PathVariable UUID id,
            @RequestBody @Valid InventoryMinimumStockUpdateDTO dto) {

        service.updateMinQuantity(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/increase", produces = JSON)
    public ResponseEntity<Void> increase(
            @PathVariable UUID id,
            @RequestBody @Valid InventoryAvailableQuantityUpdateDTO dto) {

        service.increase(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/write-off", produces = JSON)
    public ResponseEntity<Void> writeOff(
            @PathVariable UUID id,
            @RequestBody @Valid InventoryAvailableQuantityUpdateDTO dto) {

        service.writeOff(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/reserve", produces = JSON)
    public ResponseEntity<Void> reserve(
            @PathVariable UUID id,
            @RequestBody @Valid InventoryAvailableQuantityUpdateDTO dto) {

        service.reserve(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/release", produces = JSON)
    public ResponseEntity<Void> release(
            @PathVariable UUID id,
            @RequestBody @Valid InventoryAvailableQuantityUpdateDTO dto) {

        service.release(id, dto);
        return ResponseEntity.noContent().build();
    }

}


