package br.com.jogatinastore.domain.inventory.stock.service;

import br.com.jogatinastore.domain.inventory.stock.dto.StockResponseDTO;
import br.com.jogatinastore.domain.inventory.stock.entity.Stock;
import br.com.jogatinastore.domain.inventory.stock.exception.StockErrors;
import br.com.jogatinastore.domain.inventory.stock.filter.StockManagerFilter;
import br.com.jogatinastore.domain.inventory.stock.repository.StockRepository;
import br.com.jogatinastore.domain.inventory.stock.specification.StockSpecificationFactory;
import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
import br.com.jogatinastore.shared.PageResponse;
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
