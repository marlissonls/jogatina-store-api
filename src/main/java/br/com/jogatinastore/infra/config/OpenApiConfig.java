package br.com.jogatinastore.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi() {

        final String securitySchemeName = "bearerAuth";

        return
            new OpenAPI()
                .info(
                    new Info()
                        .title("JOGATINA STORE")
                        .version("v1")
                        .description("""
                            🎮 Jogatina Store Backend 🎮
                            → A Spring Boot REST API for a video game e-commerce store.
                            This project manages product inventory, user accounts, and order processing,
                            following modern Java standards and software engineering best practices.""")
                        .termsOfService("https://github.com/marlissonls/jogatina-store-api/blob/main/TERMS.md")
                        .license(
                            new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .addSecurityItem(
                    new SecurityRequirement()
                        .addList(securitySchemeName)
                )
                .schemaRequirement(
                    securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );
    }
}
