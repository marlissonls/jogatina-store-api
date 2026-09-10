package br.com.jogatinastore.integrationtests.tests.sales.checkout;

import br.com.jogatinastore.catalog.brand.domain.model.Brand;
import br.com.jogatinastore.catalog.category.domain.model.Category;
import br.com.jogatinastore.catalog.product.domain.exception.ProductErrors;
import br.com.jogatinastore.catalog.product.domain.model.Product;
import br.com.jogatinastore.customer.customer.domain.model.Customer;
import br.com.jogatinastore.iam.user.domain.model.User;
import br.com.jogatinastore.integrationtests.fixture.*;
import br.com.jogatinastore.integrationtests.infrastructure.AbstractIntegrationTest;
import br.com.jogatinastore.integrationtests.config.TestConfigs;
import br.com.jogatinastore.integrationtests.support.DatabaseCleaner;
import br.com.jogatinastore.integrationtests.support.TestAuthenticator;
import br.com.jogatinastore.inventory.stock.domain.exception.StockErrors;
import br.com.jogatinastore.inventory.stock.domain.model.Stock;
import br.com.jogatinastore.inventory.stock.infrastructure.persistence.StockRepository;
import br.com.jogatinastore.sales.cart.domain.exception.CartErrors;
import br.com.jogatinastore.sales.cart.domain.model.Cart;
import br.com.jogatinastore.sales.cart.domain.status.CartStatus;
import br.com.jogatinastore.sales.cart.infrastructure.persistence.CartRepository;
import br.com.jogatinastore.sales.checkout.application.dto.CheckoutResponseDto;
import br.com.jogatinastore.sales.order.domain.model.Order;
import br.com.jogatinastore.sales.order.domain.status.OrderStatus;
import br.com.jogatinastore.sales.order.infrastructure.persistence.OrderRepository;
import br.com.jogatinastore.shared.exception.code.ErrorCode;
import br.com.jogatinastore.shared.exception.response.ExceptionResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CheckoutIntegrationTest extends AbstractIntegrationTest {

    private final CartRepository cartRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;

    private final RoleFixture roleFixture;
    private final UserFixture userFixture;
    private final CustomerFixture customerFixture;
    private final CategoryFixture categoryFixture;
    private final BrandFixture brandFixture;
    private final ProductFixture productFixture;
    private final StockFixture stockFixture;
    private final CartFixture cartFixture;

    private final TestAuthenticator authenticator;
    private final DatabaseCleaner databaseCleaner;

    private static RequestSpecification specification;

    public CheckoutIntegrationTest(
            CartRepository cartRepository,
            StockRepository stockRepository,
            OrderRepository orderRepository,

            RoleFixture roleFixture,
            UserFixture userFixture,
            CustomerFixture customerFixture,
            CategoryFixture categoryFixture,
            BrandFixture brandFixture,
            ProductFixture productFixture,
            StockFixture stockFixture,
            CartFixture cartFixture,

            TestAuthenticator authenticator,
            DatabaseCleaner databaseCleaner
    ) {
        this.cartRepository = cartRepository;
        this.stockRepository = stockRepository;
        this.orderRepository = orderRepository;

        this.roleFixture = roleFixture;
        this.userFixture = userFixture;
        this.customerFixture = customerFixture;
        this.categoryFixture = categoryFixture;
        this.brandFixture = brandFixture;
        this.productFixture = productFixture;
        this.stockFixture = stockFixture;
        this.cartFixture = cartFixture;

        this.authenticator = authenticator;
        this.databaseCleaner = databaseCleaner;
    }

    @BeforeAll
    void setUp() {
        specification = new RequestSpecBuilder()
                .setBasePath("/api/v1/checkout")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @AfterEach
    void tearDownScenario() {
        databaseCleaner.clean();
    }


    @Test
    void shouldCreateOrderWhenCheckoutIsValid() {
        // GIVEN
        String email = "user@test.com";
        String password = "password";

        BigDecimal productSalePrice = new BigDecimal("100.00");
        BigDecimal productPrice = new BigDecimal("120.00");
        Integer stockAvailableQuantity = 100;

        Integer cartItemQuantity = 2;

        Integer expectedAvailableQuantity =
                stockAvailableQuantity - cartItemQuantity;

        Integer expectedReservedQuantity =
                cartItemQuantity;

        BigDecimal expectedTotal = productSalePrice
                .multiply(BigDecimal.valueOf(cartItemQuantity));

        User user = userFixture
                .withEmail(email)
                .withPassword(password)
                .create();

        Customer customer = customerFixture.create(user);

        Category category = categoryFixture.create();
        Brand brand = brandFixture.create();
        Product product = productFixture
                .withSalePrice(productSalePrice)
                .withPrice(productPrice)
                .withCategory(category)
                .withBrand(brand)
                .create();
        Stock stock = stockFixture
                .withProduct(product)
                .withAvailableQuantity(stockAvailableQuantity)
                .create();

        Cart cart = cartFixture.create(customer);
        cartFixture.addProduct(cart, product, cartItemQuantity);

        String token = authenticator.authenticate(email, password);


        CheckoutResponseDto response = given(specification)
                .auth()
                .oauth2(token)
                .post()
                .then()
                .statusCode(201)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .as(CheckoutResponseDto.class);

        Stock updatedStock = stockRepository.findById(stock.getId())
                .orElseThrow();

        Order createdOrder = orderRepository.findById(response.id())
                .orElseThrow();

        Cart updatedCart = cartRepository.findById(cart.getId())
                .orElseThrow();

        // ASSERT
        assertThat(response.id()).isNotNull();

        assertThat(response.customerId())
                .isEqualTo(customer.getId());

        assertThat(response.subTotalAmount())
                .isEqualByComparingTo(productSalePrice.multiply(
                        BigDecimal.valueOf(cartItemQuantity)
                ));

        assertThat(response.items()).hasSize(1);

        var item = response.items().get(0);

        assertThat(item.productId()).isEqualTo(product.getId());
        assertThat(item.quantity()).isEqualTo(cartItemQuantity);
        assertThat(item.unitPrice()).isEqualByComparingTo(productSalePrice);

        assertThat(response.totalAmount())
                .isEqualByComparingTo(expectedTotal);

        assertThat(updatedCart.getStatus()).isEqualTo(CartStatus.CONVERTED);

        assertThat(updatedStock.getAvailableQuantity())
                .isEqualTo(expectedAvailableQuantity);

        assertThat(updatedStock.getReservedQuantity())
                .isEqualTo(expectedReservedQuantity);

        assertThat(createdOrder.getStatus()).isEqualTo(OrderStatus.PENDING_PAYMENT);
    }

    @Test
    void shouldFailCheckoutWhenActiveCartIsNotFound() {
        // GIVEN
        String email = "user@test.com";
        String password = "password";

        User user = userFixture
                .withEmail(email)
                .withPassword(password)
                .create();

        Customer customer = customerFixture.create(user);

        cartFixture.create(customer, CartStatus.CONVERTED);

        String token = authenticator.authenticate(email, password);

        ExceptionResponse response = given(specification)
                .auth()
                .oauth2(token)
                .post()
                .then()
                .statusCode(404)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .as(ExceptionResponse.class);

        assertThat(response.code()).isEqualTo(ErrorCode.NOT_FOUND.name());

        assertThat(response.errors().get(0).target())
                .isEqualTo(CartErrors.Target.CART);

        assertThat(response.errors().get(0).code())
                .isEqualTo(CartErrors.Code.CART_NOT_FOUND);
    }

    @Test
    void shouldFailCheckoutWhenCartIsEmpty() {
        // GIVEN
        String email = "user@test.com";
        String password = "password";

        User user = userFixture
                .withEmail(email)
                .withPassword(password)
                .create();

        Customer customer = customerFixture.create(user);

        cartFixture.create(customer);

        String token = authenticator.authenticate(email, password);

        ExceptionResponse response = given(specification)
                .auth()
                .oauth2(token)
                .post()
                .then()
                .statusCode(409)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .as(ExceptionResponse.class);

        assertThat(response.code()).isEqualTo(ErrorCode.CART_IS_EMPTY.name());

        assertThat(response.errors().get(0).target())
                .isEqualTo(CartErrors.Target.CART);

        assertThat(response.errors().get(0).code())
                .isEqualTo(CartErrors.Code.CART_IS_EMPTY);
    }

    @Test
    void shouldFailCheckoutWhenProductIsUnavailable() {
        // GIVEN
        String email = "user@test.com";
        String password = "password";

        Integer cartItemQuantity = 1;

        User user = userFixture
                .withEmail(email)
                .withPassword(password)
                .create();

        Customer customer = customerFixture.create(user);

        Category category = categoryFixture.create();
        Brand brand = brandFixture.create();
        Product product = productFixture
                .withCategory(category)
                .withBrand(brand)
                .create();
        Stock stock = stockFixture
                .withProduct(product)
                .create();

        Cart cart = cartFixture.create(customer);
        cartFixture.addProduct(cart, product, cartItemQuantity);

        productFixture.deactivate(product);

        String token = authenticator.authenticate(email, password);

        ExceptionResponse response = given(specification)
                .auth()
                .oauth2(token)
                .post()
                .then()
                .statusCode(409)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .as(ExceptionResponse.class);

        assertThat(response.code()).isEqualTo(ErrorCode.PRODUCT_UNAVAILABLE.name());

        assertThat(response.errors().get(0).target())
                .isEqualTo(ProductErrors.Target.PRODUCT);

        assertThat(response.errors().get(0).code())
                .isEqualTo(ProductErrors.Code.PRODUCT_UNAVAILABLE);
    }

    @Test
    void shouldFailCheckoutWhenStockIsNotFound() {
        // GIVEN
        String email = "user@test.com";
        String password = "password";

        Integer stockAvailableQuantityBeforeAddToCart = 14;
        Integer stockAvailableQuantityBeforeCheckout = 7;
        Integer cartItemQuantity = 10;

        User user = userFixture
                .withEmail(email)
                .withPassword(password)
                .create();

        Customer customer = customerFixture.create(user);

        Category category = categoryFixture.create();
        Brand brand = brandFixture.create();
        Product product = productFixture
                .withCategory(category)
                .withBrand(brand)
                .create();
        Stock stock = stockFixture
                .withAvailableQuantity(stockAvailableQuantityBeforeAddToCart)
                .withProduct(product)
                .create();

        Cart cart = cartFixture.create(customer);
        cartFixture.addProduct(cart, product, cartItemQuantity);

        stockFixture.delete(stock);

        String token = authenticator.authenticate(email, password);

        ExceptionResponse response = given(specification)
                .auth()
                .oauth2(token)
                .post()
                .then()
                .statusCode(404)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .as(ExceptionResponse.class);

        assertThat(response.code()).isEqualTo(ErrorCode.NOT_FOUND.name());

        assertThat(response.errors().get(0).target())
                .isEqualTo(StockErrors.Target.PRODUCT);

        assertThat(response.errors().get(0).code())
                .isEqualTo(StockErrors.Code.STOCK_NOT_FOUND);
    }

    @Test
    void shouldFailCheckoutWhenStockIsInsufficient() {
        // GIVEN
        String email = "user@test.com";
        String password = "password";

        Integer stockAvailableQuantityBeforeAddToCart = 1;
        Integer stockAvailableQuantityBeforeCheckout = 0;
        Integer cartItemQuantity = 1;

        User user = userFixture
                .withEmail(email)
                .withPassword(password)
                .create();

        Customer customer = customerFixture.create(user);

        Category category = categoryFixture.create();
        Brand brand = brandFixture.create();
        Product product = productFixture
                .withCategory(category)
                .withBrand(brand)
                .create();
        Stock stock = stockFixture
                .withAvailableQuantity(stockAvailableQuantityBeforeAddToCart)
                .withProduct(product)
                .create();

        Cart cart = cartFixture.create(customer);
        cartFixture.addProduct(cart, product, cartItemQuantity);

        stockFixture.updateAvailableQuantity(stock, stockAvailableQuantityBeforeCheckout);

        String token = authenticator.authenticate(email, password);

        ExceptionResponse response = given(specification)
                .auth()
                .oauth2(token)
                .post()
                .then()
                .statusCode(409)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .as(ExceptionResponse.class);

        assertThat(response.code()).isEqualTo(ErrorCode.INSUFFICIENT_STOCK.name());

        assertThat(response.errors().get(0).target())
                .isEqualTo(StockErrors.Target.QUANTITY);

        assertThat(response.errors().get(0).code())
                .isEqualTo(StockErrors.Code.STOCK_QUANTITY_INSUFFICIENT);
    }
}
