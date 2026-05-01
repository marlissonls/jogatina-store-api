package br.com.jogatinastore.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final HandlerExceptionResolver resolver;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/signin"
    );

    public JwtTokenFilter(JwtTokenProvider tokenProvider, HandlerExceptionResolver resolver) {
        this.tokenProvider = tokenProvider;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        SecurityContextHolder.clearContext();

        try {
            Optional<String> tokenOpt = tokenProvider.resolveToken(request);

            if (tokenOpt.isPresent()) {
                String token = tokenOpt.get();

                Authentication auth;

                if (isRefreshEndpoint(request)) {
                    auth = tokenProvider.getRefreshAuthentication(token);
                } else {
                    auth = tokenProvider.getAccessAuthentication(token);
                }

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            chain.doFilter(request, response);

        } catch (Exception ex) {
            resolver.resolveException(request, response, null, ex);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return PUBLIC_PATHS.contains(path);
    }

    private boolean isRefreshEndpoint(HttpServletRequest request) {
        return request.getServletPath().equals("/auth/refresh");
    }
}