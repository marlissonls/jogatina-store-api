package br.com.jogatinastore.integrationtests.fixture;

import br.com.jogatinastore.catalog.product.domain.model.Product;
import br.com.jogatinastore.inventory.stock.domain.model.Stock;
import br.com.jogatinastore.inventory.stock.infrastructure.persistence.StockRepository;
import org.springframework.stereotype.Component;

@Component
public class StockFixture {

    private final StockRepository repository;

    private Product product;
    private Integer availableQuantity = 100;
    private Integer minimumQuantity = 10;

    public StockFixture(StockRepository repository) {
        this.repository = repository;
    }

    public StockFixture withProduct(Product product) {
        this.product = product;
        return this;
    }

    public StockFixture withAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
        return this;
    }

    public StockFixture withMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
        return this;
    }

    public Stock create() {
        Stock stock = new Stock(
                product,
                availableQuantity,
                minimumQuantity
        );

        return repository.save(stock);
    }

    public void updateAvailableQuantity(Stock stock, Integer stockAvailableQuantityBeforeCheckout) {
        stock.setAvailableQuantity(stockAvailableQuantityBeforeCheckout);
        repository.save(stock);
    }

    public void delete(Stock stock) {
        repository.delete(stock);
    }
}

