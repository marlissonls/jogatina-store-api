package br.com.jogatinastore.config.security.web;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public final class PublicEndpoints {

    private static final RequestMatcher MATCHER = new OrRequestMatcher(
            new AntPathRequestMatcher("/auth/signin"),
            new AntPathRequestMatcher("/auth/refresh/**"),

            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/swagger-ui/**"),

            new AntPathRequestMatcher(
                    "/api/user/v1",
                    HttpMethod.POST.name()
            ),

            new AntPathRequestMatcher(
                    "/api/products/v1/public",
                    HttpMethod.GET.name()
            ),

            new AntPathRequestMatcher(
                    "/api/products/v1/public/slug/**",
                    HttpMethod.GET.name()
            )
    );

    private PublicEndpoints() {
    }

    public static RequestMatcher matcher() {
        return MATCHER;
    }
}