package br.com.jogatinastore.domain.catalog.inventory.specifications;

import br.com.jogatinastore.domain.catalog.inventory.entity.Inventory;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class InventorySpecifications {

    public static Specification<Inventory> product(UUID productId) {
        return (root, query, cb) ->
                productId == null
                        ? null
                        : cb.equal(root.join("product").get("id"), productId);
    }

    public static Specification<Inventory> lowStock(Boolean lowStock) {
        return (root, query, cb) -> {
            if (lowStock == null) {
                return null;
            }

            Path<Integer> availableQuantity = root.get("availableQuantity");
            Path<Integer> minQuantity = root.get("minQuantity");

            return lowStock
                    ? cb.lessThanOrEqualTo(availableQuantity, minQuantity)
                    : cb.greaterThan(availableQuantity, minQuantity);
        };
    }
}
