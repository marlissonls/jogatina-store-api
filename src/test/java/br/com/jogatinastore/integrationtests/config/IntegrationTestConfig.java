package br.com.jogatinastore.integrationtests.config;

import br.com.jogatinastore.integrationtests.support.TestAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationTestConfig {

    @Bean
    public TestAuthenticator testAuthenticator() {
        return new TestAuthenticator();
    }
}
