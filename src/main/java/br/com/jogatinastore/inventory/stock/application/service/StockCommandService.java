package br.com.jogatinastore.inventory.stock.application.service;

import br.com.jogatinastore.inventory.stock.application.dto.StockAvailableQuantityUpdateDto;
import br.com.jogatinastore.inventory.stock.application.dto.StockCreateDto;
import br.com.jogatinastore.inventory.stock.application.dto.StockMinimumQuantityUpdateDto;
import br.com.jogatinastore.inventory.stock.application.dto.StockResponseDto;
import br.com.jogatinastore.inventory.stock.domain.model.Stock;
import br.com.jogatinastore.inventory.stock.application.movement.StockMovementItem;
import br.com.jogatinastore.inventory.stock.domain.exception.StockErrors;
import br.com.jogatinastore.inventory.stock.infrastructure.persistence.StockRepository;
import br.com.jogatinastore.catalog.product.application.service.ProductService;
import br.com.jogatinastore.inventory.stock.domain.exception.InsufficientStockException;
import br.com.jogatinastore.shared.exception.base.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class StockCommandService {

    private static final Logger logger = LoggerFactory.getLogger(StockCommandService.class);

    private final StockRepository repository;
    private final ProductService productService;

    public StockCommandService(
            StockRepository repository,
            ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    @Transactional
    public StockResponseDto create(StockCreateDto dto) {
        logger.debug("Creating stock. productId={}", dto.productId());

        var product = productService.getValidReference(dto.productId());

        Stock stock = Stock.createFrom(
                product,
                dto.availableQuantity(),
                dto.minimumQuantity()
        );

        Stock saved = repository.save(stock);

        logger.info("Stock created successfully. id={}, productId={}", saved.getId(), saved.getProductId());

        return new StockResponseDto(saved);
    }

    @Transactional
    public void updateMinimumQuantity(UUID id, StockMinimumQuantityUpdateDto dto) {
        logger.debug("Updating stock minimum quantity. id={}, new minimum quantity={}", id, dto.newMinimumQuantity());

        int updated = repository.updateMinimumQuantity(id, dto.newMinimumQuantity());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Stock minimum quantity successfully updated. id={}, current minimum quantity={}", id, dto.newMinimumQuantity());
    }

    @Transactional
    public void increase(UUID id, StockAvailableQuantityUpdateDto dto) {
        logger.debug("Increasing stock quantity. id={}, quantity={}", id, dto.amount());

        int updated = repository.increase(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Stock quantity increased successfully. id={}, quantity={}", id, dto.amount());
    }

    @Transactional
    public void writeOff(UUID id, StockAvailableQuantityUpdateDto dto) {
        logger.debug("Writing off stock quantity. id={}, quantity={}", id, dto.amount());

        int updated = repository.writeOff(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Stock quantity written off successfully. id={}, quantity={}", id, dto.amount());
    }

    @Transactional
    public void reserve(UUID id, StockAvailableQuantityUpdateDto dto) {
        logger.debug("Reserving stock quantity. id={}, quantity={}", id, dto.amount());

        int updated = repository.reserve(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Stock quantity reserved successfully. id={}, quantity={}", id, dto.amount());
    }

    @Transactional
    public void release(UUID id, StockAvailableQuantityUpdateDto dto) {
        logger.debug("Releasing reserved stock quantity. id={}, quantity={}", id, dto.amount());

        int updated = repository.release(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Reserved stock quantity released successfully. id={}, quantity={}", id, dto.amount());
    }

    @Transactional
    public void commit(UUID id, StockAvailableQuantityUpdateDto dto) {
        logger.debug("Committing reserved stock quantity. id={}, quantity={}", id, dto.amount());

        int updated = repository.commit(id, dto.amount());

        if (updated == 0) {
            throw new ResourceNotFoundException(StockErrors.Target.STOCK, StockErrors.Code.STOCK_NOT_FOUND);
        }

        logger.info("Reserved stock quantity committed successfully. id={}, quantity={}", id, dto.amount());
    }

    public void reserveItems(@Valid List<StockMovementItem> items) {
        for (StockMovementItem item : items) {
            Stock stock = repository.findByProductId(item.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            StockErrors.Target.PRODUCT,
                            StockErrors.Code.STOCK_NOT_FOUND
                    ));

            if (stock.getAvailableQuantity() < item.quantity()) {
                throw new InsufficientStockException(
                        StockErrors.Target.QUANTITY,
                        StockErrors.Code.STOCK_QUANTITY_INSUFFICIENT
                );
            }

            int updatedRows = repository.reserve(stock.getId(), item.quantity());

            if (updatedRows == 0) {
                throw new InsufficientStockException(
                        StockErrors.Target.QUANTITY,
                        StockErrors.Code.STOCK_QUANTITY_INSUFFICIENT
                );
            }
        }
    }

    public void releaseItems(@Valid List<StockMovementItem> items) {
        for (StockMovementItem item: items) {
            Stock stock = repository.findByProductId(item.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            StockErrors.Target.PRODUCT,
                            StockErrors.Code.STOCK_NOT_FOUND
                    ));

            if (stock.getReservedQuantity() < item.quantity()) {
                throw new InsufficientStockException(
                        StockErrors.Target.RESERVED_QUANTITY,
                        StockErrors.Code.STOCK_RESERVED_QUANTITY_INSUFFICIENT
                );
            }

            int updatedRows = repository.release(stock.getId(), item.quantity());

            if (updatedRows == 0) {
                throw new InsufficientStockException(
                        StockErrors.Target.RESERVED_QUANTITY,
                        StockErrors.Code.STOCK_RESERVED_QUANTITY_INSUFFICIENT
                );
            }
        }
    }
}
