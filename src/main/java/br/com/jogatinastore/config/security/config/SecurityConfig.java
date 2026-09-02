package br.com.jogatinastore.config.security.config;

import br.com.jogatinastore.config.security.web.ProtectedEndpoints;
import br.com.jogatinastore.config.security.web.PublicEndpoints;
import br.com.jogatinastore.observability.logging.LoggingFilter;
import br.com.jogatinastore.config.security.web.RequestAuthenticationFilter;
import br.com.jogatinastore.config.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final HandlerExceptionResolver resolver;
    private final List<String> allowedOrigins;

    public SecurityConfig(
            JwtTokenProvider tokenProvider,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
            @Value("${cors.originPatterns}") List<String> allowedOrigins
    ) {
        this.tokenProvider = tokenProvider;
        this.resolver = resolver;
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        RequestAuthenticationFilter filter = new RequestAuthenticationFilter(tokenProvider, resolver);
        LoggingFilter loggingFilter = new LoggingFilter();

        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection for stateless API requests
            .cors(Customizer.withDefaults())
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(
                authorizeHttpRequests -> authorizeHttpRequests
                    .requestMatchers(PublicEndpoints.matcher()).permitAll()
                    .requestMatchers(ProtectedEndpoints.matcher()).authenticated()
                    .anyRequest().permitAll() // Ensures unknown routes reach MVC and result in NoResourceFoundException instead of InsufficientAuthenticationException
            )
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint((request, response, authException) -> {
                    resolver.resolveException(request, response, null, authException);
                })
            )
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // Cache da resposta CORS por 1 hora (melhora performance)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
