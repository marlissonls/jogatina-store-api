package br.com.jogatinastore.inventory.stock.application.service;

import br.com.jogatinastore.inventory.stock.application.dto.StockResponseDTO;
import br.com.jogatinastore.inventory.stock.domain.model.Stock;
import br.com.jogatinastore.inventory.stock.domain.exception.StockErrors;
import br.com.jogatinastore.inventory.stock.presentation.filter.StockManagerFilter;
import br.com.jogatinastore.inventory.stock.infrastructure.persistence.StockRepository;
import br.com.jogatinastore.inventory.stock.infrastructure.specification.StockSpecificationFactory;
import br.com.jogatinastore.shared.exception.base.ResourceNotFoundException;
import br.com.jogatinastore.shared.pagination.PageResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockQueryService {

    private static final Logger logger = LoggerFactory.getLogger(StockQueryService.class);

    private final StockRepository repository;

    public StockQueryService(StockRepository repository) {
        this.repository = repository;
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

    public List<Stock> findByProductIdIn(List<UUID> ids) {
        return repository.findByProductIdIn(ids);
    }

    private Stock findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Stock not found. id={}", id);
                    return new ResourceNotFoundException(StockErrors.Target.ID, StockErrors.Code.STOCK_NOT_FOUND);
                });
    }
}
