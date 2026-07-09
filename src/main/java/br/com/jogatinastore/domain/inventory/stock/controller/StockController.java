package br.com.jogatinastore.domain.inventory.stock.controller;

import br.com.jogatinastore.domain.inventory.stock.dto.StockCreateDTO;
import br.com.jogatinastore.domain.inventory.stock.dto.StockResponseDTO;
import br.com.jogatinastore.domain.inventory.stock.dto.StockMinimumQuantityUpdateDTO;
import br.com.jogatinastore.domain.inventory.stock.dto.StockAvailableQuantityUpdateDTO;
import br.com.jogatinastore.domain.inventory.stock.filter.StockManagerFilter;
import br.com.jogatinastore.domain.inventory.stock.service.StockCommandService;
import br.com.jogatinastore.domain.inventory.stock.service.StockQueryService;
import br.com.jogatinastore.shared.PageResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/stocks/v1")
@PreAuthorize("hasRole('MANAGER')")
public class StockController {

    private final StockCommandService commandService;
    private final StockQueryService queryService;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public StockController(
            StockQueryService queryService,
            StockCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @PostMapping(consumes = JSON, produces = JSON)
    public ResponseEntity<StockResponseDTO> create(@RequestBody @Valid StockCreateDTO dto) {
        var response = commandService.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<PageResponse<StockResponseDTO>> findAll(
            @ParameterObject @ModelAttribute StockManagerFilter filter,
            @PageableDefault(size = 12, sort = "availableQuantity") Pageable pageable
    ) {
        return ResponseEntity.ok().body(queryService.findAll(
                filter, pageable
        ));
    }

    @GetMapping(path = "/{id}", produces = JSON)
    public ResponseEntity<StockResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(queryService.findById(id));
    }

    @PatchMapping(path = "/{id}/minimum-stock", produces = JSON)
    public ResponseEntity<Void> updateMinimumQuantity(
            @PathVariable UUID id,
            @RequestBody @Valid StockMinimumQuantityUpdateDTO dto) {

        commandService.updateMinimumQuantity(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/increase", produces = JSON)
    public ResponseEntity<Void> increase(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDTO dto) {

        commandService.increase(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/write-off", produces = JSON)
    public ResponseEntity<Void> writeOff(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDTO dto) {

        commandService.writeOff(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/reserve", produces = JSON)
    public ResponseEntity<Void> reserve(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDTO dto) {

        commandService.reserve(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/release", produces = JSON)
    public ResponseEntity<Void> release(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDTO dto) {

        commandService.release(id, dto);
        return ResponseEntity.noContent().build();
    }

}


