package br.com.jogatinastore.domain.catalog.inventory.service;

import br.com.jogatinastore.domain.catalog.inventory.dto.InventoryCreateDTO;
import br.com.jogatinastore.domain.catalog.inventory.dto.InventoryMinimumStockUpdateDTO;
import br.com.jogatinastore.domain.catalog.inventory.dto.InventoryResponseDTO;
import br.com.jogatinastore.domain.catalog.inventory.dto.InventoryAvailableQuantityUpdateDTO;
import br.com.jogatinastore.domain.catalog.inventory.entity.Inventory;
import br.com.jogatinastore.domain.catalog.inventory.exception.InventoryErrors;
import br.com.jogatinastore.domain.catalog.inventory.repository.InventoryRepository;
import br.com.jogatinastore.domain.catalog.inventory.specifications.InventorySpecifications;
import br.com.jogatinastore.domain.catalog.product.service.ProductService;
import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
import br.com.jogatinastore.shared.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository repository;
    private final ProductService productService;

    public InventoryService(
            InventoryRepository repository,
            ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    public InventoryResponseDTO create(InventoryCreateDTO dto) {

        var product = productService.getValidReference(dto.productId());

        Inventory inventory = Inventory.createFrom(
                product,
                dto.availableQuantity(),
                dto.minQuantity()
        );
        return new InventoryResponseDTO(inventory);
    }

    public PageResponse<InventoryResponseDTO> findAll(
            UUID product,
            Boolean lowStock,
            Pageable pageable
    ) {
        logger.debug("Fetching filtered inventories");

        Specification<Inventory> spec = Specification
            .where(InventorySpecifications.product(product))
            .and(InventorySpecifications.lowStock(lowStock));

        Page<Inventory> page = repository.findAll(spec, pageable);

        var items = page.stream().map(InventoryResponseDTO::new).toList();

        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    public InventoryResponseDTO findById(UUID id) {
        return new InventoryResponseDTO(findEntityById(id));
    }

    public void updateMinQuantity(UUID id, InventoryMinimumStockUpdateDTO dto) {
        int updated = repository.updateMinQuantity(id, dto.newMinQuantity());

        if (updated == 0) {
            throw new ResourceNotFoundException(InventoryErrors.Target.INVENTORY, InventoryErrors.Code.INVENTORY_NOT_FOUND);
        }
    }

    @Transactional
    public void increase(UUID id, InventoryAvailableQuantityUpdateDTO dto) {
        int updated = repository.increase(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(InventoryErrors.Target.INVENTORY, InventoryErrors.Code.INVENTORY_NOT_FOUND);
        }
    }

    @Transactional
    public void writeOff(UUID id, InventoryAvailableQuantityUpdateDTO dto) {
        int updated = repository.writeOff(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(InventoryErrors.Target.INVENTORY, InventoryErrors.Code.INVENTORY_NOT_FOUND);
        }
    }

    @Transactional
    public void reserve(UUID id, InventoryAvailableQuantityUpdateDTO dto) {
        int updated = repository.reserve(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(InventoryErrors.Target.INVENTORY, InventoryErrors.Code.INVENTORY_NOT_FOUND);
        }
    }

    @Transactional
    public void release(UUID id, InventoryAvailableQuantityUpdateDTO dto) {
        int updated = repository.release(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(InventoryErrors.Target.INVENTORY, InventoryErrors.Code.INVENTORY_NOT_FOUND);
        }
    }

    @Transactional
    public void commit(UUID id, InventoryAvailableQuantityUpdateDTO dto) {
        int updated = repository.commit(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(InventoryErrors.Target.INVENTORY, InventoryErrors.Code.INVENTORY_NOT_FOUND);
        }
    }

    private Inventory findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Inventory not found. id={}", id);
                    return new ResourceNotFoundException(InventoryErrors.Target.ID, InventoryErrors.Code.INVENTORY_NOT_FOUND);
                });
    }
}
