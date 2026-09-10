package br.com.jogatinastore.integrationtests.fixture;

import br.com.jogatinastore.catalog.product.domain.model.Product;
import br.com.jogatinastore.customer.customer.domain.model.Customer;
import br.com.jogatinastore.sales.cart.domain.model.Cart;
import br.com.jogatinastore.sales.cart.domain.status.CartStatus;
import br.com.jogatinastore.sales.cart.infrastructure.persistence.CartRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class CartFixture {

    private final CartRepository repository;

    public CartFixture(CartRepository repository) {
        this.repository = repository;
    }

    public Cart create(Customer customer) {
        return create(customer, CartStatus.ACTIVE);
    }

    public Cart create(Customer customer, CartStatus status) {
        Cart cart = new Cart(customer.getId());
        cart.setStatus(status);

        return repository.save(cart);
    }

    @Transactional
    public void addProduct(Cart cart, Product product, Integer quantity) {
        Cart managedCart = repository.findById(cart.getId())
                .orElseThrow();

        BigDecimal unitPrice = product.getSalePrice() != null
                && product.getSalePrice().compareTo(product.getPrice()) < 0
                ? product.getSalePrice()
                : product.getPrice();

        managedCart.addItem(
                product.getId(),
                unitPrice,
                quantity
        );
    }
}
