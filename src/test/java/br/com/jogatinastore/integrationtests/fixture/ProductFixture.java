package br.com.jogatinastore.integrationtests.fixture;

import br.com.jogatinastore.catalog.brand.domain.model.Brand;
import br.com.jogatinastore.catalog.category.domain.model.Category;
import br.com.jogatinastore.catalog.product.domain.model.Product;
import br.com.jogatinastore.catalog.product.infrastructure.persistence.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductFixture {

    private final ProductRepository repository;

    private String title = "Produto Teste";
    private String slug = "produto-teste";
    private String description = "Descrição do produto";
    private BigDecimal price = new BigDecimal("10.00");
    private BigDecimal salePrice = new BigDecimal("10.00");
    private BigDecimal costPrice = new BigDecimal("10.00");
    private String barcode = "7891234567890";
    private String sku = "SKU-001";
    private Category category;
    private Brand brand;

    public ProductFixture(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductFixture withTitle(String title) {
        this.title = title;
        return this;
    }

    public ProductFixture withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public ProductFixture withDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductFixture withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductFixture withSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public ProductFixture withCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
        return this;
    }

    public ProductFixture withBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public ProductFixture withSku(String sku) {
        this.sku = sku;
        return this;
    }

    public ProductFixture withCategory(Category category) {
        this.category = category;
        return this;
    }

    public ProductFixture withBrand(Brand brand) {
        this.brand = brand;
        return this;
    }

    public Product create() {
        Product product = new Product(
                title,
                slug,
                description,
                price,
                salePrice,
                costPrice,
                barcode,
                sku,
                category,
                brand
        );

        return repository.save(product);
    }

    public void deactivate(Product product) {
        product.setActive(false);
        repository.save(product);
    }

    public void activate(Product product) {
        product.setActive(false);
        repository.save(product);
    }

    public void updateSalePrice(Product product, BigDecimal updatedSalePrice) {
        product.setSalePrice(updatedSalePrice);
        repository.save(product);
    }
}

