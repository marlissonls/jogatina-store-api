package br.com.jogatinastore.domain.catalog.product.specification;

import br.com.jogatinastore.domain.catalog.product.entity.Product;
import br.com.jogatinastore.domain.catalog.product.filter.ProductManagerFilter;
import br.com.jogatinastore.domain.catalog.product.filter.ProductPublicFilter;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecificationFactory {

    private ProductSpecificationFactory() {}

    public static Specification<Product> publicView(ProductPublicFilter filter) {
        Specification<Product> spec = Specification.where(null);

        spec = spec.and(ProductSpecifications.search(filter.search()));

        spec = spec.and(ProductSpecifications.category(filter.category()));
        spec = spec.and(ProductSpecifications.brand(filter.brand()));

        spec = spec.and(ProductSpecifications.onSale(filter.onSale()));
        spec = spec.and(ProductSpecifications.featured(filter.featured()));
        spec = spec.and(ProductSpecifications.isNew(filter.isNew()));

        spec = spec.and(ProductSpecifications.minPrice(filter.minPrice()));
        spec = spec.and(ProductSpecifications.maxPrice(filter.maxPrice()));

        //spec = spec.and(ProductSpecifications.availableForSale(true));

        return spec;
    }

    public static Specification<Product> managerView(ProductManagerFilter filter) {
        Specification<Product> spec = Specification.where(null);

        spec = spec.and(ProductSpecifications.search(filter.search()));

        spec = spec.and(ProductSpecifications.category(filter.category()));
        spec = spec.and(ProductSpecifications.brand(filter.brand()));

        spec = spec.and(ProductSpecifications.onSale(filter.onSale()));
        spec = spec.and(ProductSpecifications.featured(filter.featured()));
        spec = spec.and(ProductSpecifications.isNew(filter.isNew()));

        spec = spec.and(ProductSpecifications.minPrice(filter.minPrice()));
        spec = spec.and(ProductSpecifications.maxPrice(filter.maxPrice()));
        spec = spec.and(ProductSpecifications.minCost(filter.minCost()));
        spec = spec.and(ProductSpecifications.maxCost(filter.maxCost()));

        spec = spec.and(ProductSpecifications.active(filter.active()));
//        spec = spec.and(ProductSpecifications.hasAvailableStock(filter.hasAvailableStock()));

        spec = spec.and(ProductSpecifications.createdFrom(filter.createdFrom()));
        spec = spec.and(ProductSpecifications.createdTo(filter.createdTo()));
        spec = spec.and(ProductSpecifications.updatedFrom(filter.updatedFrom()));
        spec = spec.and(ProductSpecifications.updatedTo(filter.updatedTo()));

        return spec;
    }
}
