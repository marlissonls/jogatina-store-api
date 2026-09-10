package br.com.jogatinastore.integrationtests.support;

import br.com.jogatinastore.integrationtests.config.TestConfigs;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

public class TestAuthenticator {

    private final RequestSpecification specification;

    public TestAuthenticator() {
        this.specification = new RequestSpecBuilder()
                .setBasePath("/auth")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    public String authenticate(String username, String password) {
        return given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                    """.formatted(username, password))
                .post("/signin")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }
}