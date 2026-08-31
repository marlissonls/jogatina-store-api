package br.com.jogatinastore.inventory.stock.presentation.http;

import br.com.jogatinastore.inventory.stock.application.dto.StockCreateDTO;
import br.com.jogatinastore.inventory.stock.application.dto.StockResponseDTO;
import br.com.jogatinastore.inventory.stock.application.dto.StockMinimumQuantityUpdateDTO;
import br.com.jogatinastore.inventory.stock.application.dto.StockAvailableQuantityUpdateDTO;
import br.com.jogatinastore.inventory.stock.presentation.docs.StockControllerDocs;
import br.com.jogatinastore.inventory.stock.presentation.filter.StockManagerFilter;
import br.com.jogatinastore.inventory.stock.application.service.StockCommandService;
import br.com.jogatinastore.inventory.stock.application.service.StockQueryService;
import br.com.jogatinastore.shared.pagination.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/stocks")
@PreAuthorize("hasRole('MANAGER')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Stocks", description = "Endpoints for Stocks management")
public class StockController implements StockControllerDocs {

    private final StockCommandService commandService;
    private final StockQueryService queryService;

    public StockController(
            StockQueryService queryService,
            StockCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockResponseDTO> create(@RequestBody @Valid StockCreateDTO dto) {
        var response = commandService.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<StockResponseDTO>> findAll(
            @ParameterObject @ModelAttribute StockManagerFilter filter,
            @PageableDefault(size = 12, sort = "availableQuantity") Pageable pageable
    ) {
        return ResponseEntity.ok().body(queryService.findAll(
                filter, pageable
        ));
    }

    @Override
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(queryService.findById(id));
    }

    @Override
    @PatchMapping(path = "/{id}/minimum-stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateMinimumQuantity(
            @PathVariable UUID id,
            @RequestBody @Valid StockMinimumQuantityUpdateDTO dto) {

        commandService.updateMinimumQuantity(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(path = "/{id}/increase", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> increase(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDTO dto) {

        commandService.increase(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(path = "/{id}/write-off", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> writeOff(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDTO dto) {

        commandService.writeOff(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(path = "/{id}/reserve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> reserve(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDTO dto) {

        commandService.reserve(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(path = "/{id}/release", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> release(
            @PathVariable UUID id,
            @RequestBody @Valid StockAvailableQuantityUpdateDTO dto) {

        commandService.release(id, dto);
        return ResponseEntity.noContent().build();
    }

}


