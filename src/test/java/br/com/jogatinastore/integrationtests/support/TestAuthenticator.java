package br.com.jogatinastore.integrationtests.support;

import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class TestAuthenticator {

    private final RequestSpecification specification;

    public TestAuthenticator(RequestSpecification authenticationSpecification) {
        this.specification = authenticationSpecification;
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