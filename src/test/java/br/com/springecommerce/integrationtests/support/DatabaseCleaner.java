package br.com.springecommerce.integrationtests.support;

import br.com.springecommerce.catalog.brand.infrastructure.persistence.BrandRepository;
import br.com.springecommerce.catalog.category.infrastructure.persistence.CategoryRepository;
import br.com.springecommerce.catalog.product.infrastructure.persistence.ProductRepository;
import br.com.springecommerce.customer.customer.infrastructure.persistence.CustomerRepository;
import br.com.springecommerce.iam.role.infrastructure.persistence.RoleRepository;
import br.com.springecommerce.iam.user.infrastructure.persistence.UserRepository;
import br.com.springecommerce.inventory.stock.infrastructure.persistence.StockRepository;
import br.com.springecommerce.sales.cart.infrastructure.persistence.repository.CartRepository;
import br.com.springecommerce.sales.order.infrastructure.persistence.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public DatabaseCleaner(
            CartRepository cartRepository,
            OrderRepository orderRepository,
            StockRepository stockRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            BrandRepository brandRepository,
            CustomerRepository customerRepository,
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void clean() {
        cartRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
        brandRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        roleRepository.deleteAllInBatch();
    }
}

