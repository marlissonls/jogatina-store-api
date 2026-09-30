package br.com.jogatinastore.integrationtests.config;

import br.com.jogatinastore.integrationtests.support.TestAuthenticator;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationTestConfig {

    @Bean
    RequestSpecification authenticationSpecification() {
        return new RequestSpecBuilder()
                .setBasePath("/auth")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Bean
    TestAuthenticator testAuthenticator(
            RequestSpecification authenticationSpecification
    ) {
        return new TestAuthenticator(authenticationSpecification);
    }
}
