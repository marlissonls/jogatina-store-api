package br.com.jogatinastore.infra.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

public class JwtTokenFilter extends OncePerRequestFilter {

    private static final List<RequestMatcher> PUBLIC_ENDPOINTS = List.of(
            new AntPathRequestMatcher("/auth/signin"),
            new AntPathRequestMatcher("/auth/refresh"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/swagger-ui/**"),

            // Public user registration
            new AntPathRequestMatcher("/api/user/v1", HttpMethod.POST.name())
    );

    private final JwtTokenProvider tokenProvider;
    private final HandlerExceptionResolver resolver;

    public JwtTokenFilter(
            JwtTokenProvider tokenProvider,
            HandlerExceptionResolver resolver
    ) {
        this.tokenProvider = tokenProvider;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        try {
            tokenProvider.resolveToken(request)
                    .map(tokenProvider::getAccessAuthentication)
                    .ifPresent(auth ->
                            SecurityContextHolder.getContext()
                                    .setAuthentication(auth)
                    );

            chain.doFilter(request, response);

        } catch (Exception ex) {
            resolver.resolveException(request, response, null, ex);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PUBLIC_ENDPOINTS.stream()
                .anyMatch(matcher -> matcher.matches(request));
    }
}