package br.com.jogatinastore.config;

import br.com.jogatinastore.security.jwt.JwtTokenFilter;
import br.com.jogatinastore.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final HandlerExceptionResolver resolver;

    public SecurityConfig(
            JwtTokenProvider tokenProvider,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
    ) {
        this.tokenProvider = tokenProvider;
        this.resolver = resolver;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter filter = new JwtTokenFilter(tokenProvider, resolver);

        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF para conseguir dar POST/PUT
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(
                authorizeHttpRequests -> authorizeHttpRequests
                    .requestMatchers(
                        "/auth/signin",
                        "/auth/refresh/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                    ).permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/users").denyAll()

            )
            .cors(cors -> {})
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint((request, response, authException) -> {
                    resolver.resolveException(request, response, null, authException);
                })
            )
            .build();
    }
}
