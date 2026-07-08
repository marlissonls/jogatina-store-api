package br.com.jogatinastore.domain.inventory.stock.specification;

import br.com.jogatinastore.domain.inventory.stock.entity.Stock;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class StockSpecifications {

    public static Specification<Stock> product(UUID productId) {
        return (root, query, cb) ->
                productId == null
                        ? null
                        : cb.equal(root.join("product").get("id"), productId);
    }

    public static Specification<Stock> lowStock(Boolean lowStock) {
        return (root, query, cb) -> {
            if (lowStock == null) {
                return null;
            }

            Path<Integer> availableQuantity = root.get("availableQuantity");
            Path<Integer> minimumQuantity = root.get("minimumQuantity");

            return lowStock
                    ? cb.lessThanOrEqualTo(availableQuantity, minimumQuantity)
                    : cb.greaterThan(availableQuantity, minimumQuantity);
        };
    }
}
