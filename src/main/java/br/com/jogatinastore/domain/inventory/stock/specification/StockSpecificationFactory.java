package br.com.jogatinastore.domain.inventory.stock.specification;

import br.com.jogatinastore.domain.inventory.stock.entity.Stock;
import br.com.jogatinastore.domain.inventory.stock.filter.StockManagerFilter;
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
