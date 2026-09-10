package br.com.jogatinastore.integrationtests.tests.cors;

import br.com.jogatinastore.integrationtests.infrastructure.AbstractIntegrationTest;
import br.com.jogatinastore.integrationtests.config.TestConfigs;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CorsIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;

    @BeforeAll
    void setUp() {

        specification = new RequestSpecBuilder()
                .setBasePath("/api/v1/products")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    void shouldAllowOrigin() {

        given(specification)
                .header(
                        TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_LOCAL
                )
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(
                        "Access-Control-Allow-Origin",
                        TestConfigs.ORIGIN_LOCAL
                );
    }

    @Test
    void shouldBlockOrigin() {

        String blockedOrigin = "http://malicious-site.com";

        given(specification)
                .header(
                        TestConfigs.HEADER_PARAM_ORIGIN,
                        blockedOrigin
                )
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(equalTo("Invalid CORS request"));;
    }
}
