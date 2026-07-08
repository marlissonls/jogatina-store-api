package br.com.jogatinastore.domain.inventory.stock.service;

import br.com.jogatinastore.domain.inventory.stock.dto.StockCreateDTO;
import br.com.jogatinastore.domain.inventory.stock.dto.StockMinimumQuantityUpdateDTO;
import br.com.jogatinastore.domain.inventory.stock.dto.StockResponseDTO;
import br.com.jogatinastore.domain.inventory.stock.dto.StockAvailableQuantityUpdateDTO;
import br.com.jogatinastore.domain.inventory.stock.entity.Stock;
import br.com.jogatinastore.domain.inventory.stock.exception.StockErrors;
import br.com.jogatinastore.domain.inventory.stock.filter.StockManagerFilter;
import br.com.jogatinastore.domain.inventory.stock.repository.StockRepository;
import br.com.jogatinastore.domain.inventory.stock.specification.StockSpecificationFactory;
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
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    private final StockRepository repository;
    private final ProductService productService;

    public StockService(
            StockRepository repository,
            ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    @Transactional
    public StockResponseDTO create(StockCreateDTO dto) {
        logger.debug("Creating stock. productId={}", dto.productId());

        var product = productService.getValidReference(dto.productId());

        Stock stock = Stock.createFrom(
                product,
                dto.availableQuantity(),
                dto.minimumQuantity()
        );

        Stock saved = repository.save(stock);

        logger.info("Stock created successfully. id={}, productId={}", saved.getId(), saved.getProductId());

        return new StockResponseDTO(saved);
    }

    public PageResponse<StockResponseDTO> findAll(
            StockManagerFilter filter,
            Pageable pageable
    ) {
        logger.debug("Searching stocks. filter={}, pageable={}", filter, pageable);

        Specification<Stock> spec = StockSpecificationFactory.managerView(filter);

        Page<StockResponseDTO> page = repository.findAll(spec, pageable)
                .map(StockResponseDTO::new);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    public StockResponseDTO findById(UUID id) {
        logger.debug("Loading stock. id={}", id);

        return new StockResponseDTO(findEntityById(id));
    }

    @Transactional
    public void updateMinimumQuantity(UUID id, StockMinimumQuantityUpdateDTO dto) {
        logger.debug("Updating stock minimum quantity. id={}, new minimum quantity={}", id, dto.newMinimumQuantity());

        int updated = repository.updateMinimumQuantity(id, dto.newMinimumQuantity());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Stock minimum quantity successfully updated. id={}, current minimum quantity={}", id, dto.newMinimumQuantity());
    }

    @Transactional
    public void increase(UUID id, StockAvailableQuantityUpdateDTO dto) {
        logger.debug("Increasing stock quantity. id={}, quantity={}", id, dto.amount());

        int updated = repository.increase(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Stock quantity increased successfully. id={}, quantity={}", id, dto.amount());
    }

    @Transactional
    public void writeOff(UUID id, StockAvailableQuantityUpdateDTO dto) {
        logger.debug("Writing off stock quantity. id={}, quantity={}", id, dto.amount());

        int updated = repository.writeOff(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Stock quantity written off successfully. id={}, quantity={}", id, dto.amount());
    }

    @Transactional
    public void reserve(UUID id, StockAvailableQuantityUpdateDTO dto) {
        logger.debug("Reserving stock quantity. id={}, quantity={}", id, dto.amount());

        int updated = repository.reserve(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Stock quantity reserved successfully. id={}, quantity={}", id, dto.amount());
    }

    @Transactional
    public void release(UUID id, StockAvailableQuantityUpdateDTO dto) {
        logger.debug("Releasing reserved stock quantity. id={}, quantity={}", id, dto.amount());

        int updated = repository.release(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Reserved stock quantity released successfully. id={}, quantity={}", id, dto.amount());
    }

    @Transactional
    public void commit(UUID id, StockAvailableQuantityUpdateDTO dto) {
        logger.debug("Committing reserved stock quantity. id={}, quantity={}", id, dto.amount());

        int updated = repository.commit(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Reserved stock quantity committed successfully. id={}, quantity={}", id, dto.amount());
    }

    private Stock findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Stock not found. id={}", id);
                    return new ResourceNotFoundException(StockErrors.Target.ID, StockErrors.Code.STOCK_NOT_FOUND);
                });
    }
}
