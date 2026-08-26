package br.com.jogatinastore.infra.web;

import br.com.jogatinastore.infra.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

public class RequestAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final HandlerExceptionResolver resolver;

    public RequestAuthenticationFilter(
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
        return PublicEndpoints.matcher().matches(request);
    }
}