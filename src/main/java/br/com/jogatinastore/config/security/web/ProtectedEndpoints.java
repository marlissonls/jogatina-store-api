package br.com.jogatinastore.config.security.web;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public final class ProtectedEndpoints {

    private static final RequestMatcher MATCHER = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/v1/brands/**"),
            new AntPathRequestMatcher("/api/v1/categories/**"),
            new AntPathRequestMatcher("/api/v1/products/**"),
            new AntPathRequestMatcher("/api/v1/customers/**"),
            new AntPathRequestMatcher("/api/v1/authentication/**"),
            new AntPathRequestMatcher("/api/v1/roles/**"),
            new AntPathRequestMatcher("/api/v1/users/**"),
            new AntPathRequestMatcher("/api/v1/carts/**"),
            new AntPathRequestMatcher("/api/v1/checkout/**"),
            new AntPathRequestMatcher("/api/v1/orders/**")
    );

    private ProtectedEndpoints() {}

    public static RequestMatcher matcher() {
        return MATCHER;
    }
}
