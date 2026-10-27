package br.com.springecommerce.inventory.stock.infrastructure.specification;

import br.com.springecommerce.inventory.stock.domain.model.Stock;
import br.com.springecommerce.inventory.stock.presentation.filter.StockManagerFilter;
import org.springframework.data.jpa.domain.Specification;

public class StockSpecificationFactory {

    private StockSpecificationFactory() {}

    public static Specification<Stock> managerView(StockManagerFilter filter) {
        Specification<Stock> spec = Specification.where(null);

        spec = spec.and(StockSpecifications.product(filter.product()));
        spec = spec.and(StockSpecifications.lowStock(filter.lowStock()));

        return spec;
    }
}
