package br.com.jogatinastore.domain.catalog.product.specifications;

import br.com.jogatinastore.domain.catalog.inventory.entity.Inventory;
import br.com.jogatinastore.domain.catalog.product.entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ProductSpecifications {

    public static Specification<Product> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return null;
            }

            String term = "%" + search.toLowerCase().trim() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), term),
                    cb.like(cb.lower(root.get("description")), term),
                    cb.like(cb.lower(root.get("sku")), term),
                    cb.like(cb.lower(root.join("brand").get("name")), term),
                    cb.like(cb.lower(root.join("category").get("name")), term)
            );
        };
    }

    public static Specification<Product> active(Boolean active) {
        return (root, query, cb) ->
                active == null
                        ? null
                        : cb.equal(root.get("active"), active);
    }

    public static Specification<Product> featured(Boolean featured) {
        return (root, query, cb) ->
                featured == null
                        ? null
                        : cb.equal(root.get("featured"), featured);
    }

    public static Specification<Product> isNew(Boolean isNew) {
        return (root, query, cb) -> {

            if (isNew == null) {
                return null;
            }

            LocalDateTime limit = LocalDateTime.now().minusDays(30);

            return isNew
                    ? cb.greaterThanOrEqualTo(root.get("createdAt"), limit)
                    : cb.lessThan(root.get("createdAt"), limit);
        };
    }

    public static Specification<Product> category(UUID categoryId) {
        return (root, query, cb) ->
                categoryId == null
                        ? null
                        : cb.equal(root.join("category").get("id"), categoryId);
    }

    public static Specification<Product> brand(UUID brandId) {
        return (root, query, cb) ->
                brandId == null
                        ? null
                        : cb.equal(root.join("brand").get("id"), brandId);
    }

    public static Specification<Product> minPrice(BigDecimal minPrice) {
        return (root, query, cb) ->
                minPrice == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice == null
                        ? null
                        : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> minCost(BigDecimal minCost) {
        return (root, query, cb) ->
                minCost == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("costPrice"), minCost);
    }

    public static Specification<Product> maxCost(BigDecimal maxCost) {
        return (root, query, cb) ->
                maxCost == null
                        ? null
                        : cb.lessThanOrEqualTo(root.get("costPrice"), maxCost);
    }

//    public static Specification<Product> availableForSale(Boolean availableForSale) {
//        return (root, query, cb) -> {
//
//            if (availableForSale == null) {
//                return null;
//            }
//
//            var inventory = root.join("inventory");
//
//            var available = cb.diff(
//                    inventory.get("availableQuantity"),
//                    inventory.get("reservedQuantity")
//            );
//
//            return availableForSale
//                    ? cb.gt(available, 0)
//                    : cb.le(available, 0);
//        };
//    }

    public static Specification<Product> availableForSale(Boolean availableForSale) {
        return (root, query, cb) -> {

            if (availableForSale == null) {
                return null;
            }

            Join<Product, Inventory> inventory = root.join("inventory");
            Path<Integer> availableQuantity = inventory.get("availableQuantity");

            return availableForSale
                    ? cb.gt(availableQuantity, 0)
                    : cb.le(availableQuantity, 0);
        };
    }

    public static Specification<Product> hasInventory(Boolean hasInventory) {
        return (root, query, cb) -> {

            if (hasInventory == null) {
                return null;
            }

            var inventory = root.join("inventory");

            return hasInventory
                    ? cb.greaterThan(inventory.get("availableQuantity"), 0)
                    : cb.lessThanOrEqualTo(inventory.get("availableQuantity"), 0);
        };
    }

    public static Specification<Product> onSale(Boolean onSale) {
        return (root, query, cb) -> {
            if (onSale == null) {
                return null;
            }

            return onSale
                    ? cb.isNotNull(root.get("salePrice"))
                    : cb.isNull(root.get("salePrice"));
        };
    }

    public static Specification<Product> createdFrom(LocalDateTime createdFrom) {
        return (root, query, cb) ->
            createdFrom == null
                ? null
                : cb.greaterThanOrEqualTo(
                    root.get("createdAt"),
                    createdFrom
                );
    }

    public static Specification<Product> createdTo(LocalDateTime createdTo) {
        return (root, query, cb) ->
            createdTo == null
                ? null
                : cb.lessThanOrEqualTo(
                    root.get("createdAt"),
                    createdTo
                );
    }

    public static Specification<Product> updatedFrom(LocalDateTime updatedFrom) {
        return (root, query, cb) ->
            updatedFrom == null
                ? null
                : cb.greaterThanOrEqualTo(
                    root.get("updatedAt"),
                    updatedFrom
                );
    }

    public static Specification<Product> updatedTo(LocalDateTime updatedTo) {
        return (root, query, cb) ->
            updatedTo == null
                ? null
                : cb.lessThanOrEqualTo(
                    root.get("updatedAt"),
                    updatedTo
                );
    }
}

