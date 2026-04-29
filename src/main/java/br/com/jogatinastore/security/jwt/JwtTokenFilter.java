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

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final HandlerExceptionResolver resolver;

    public JwtTokenFilter(JwtTokenProvider tokenProvider, HandlerExceptionResolver resolver) {
        this.tokenProvider = tokenProvider;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
        try {
            String headerToken = tokenProvider.resolveToken(request);

            if (headerToken != null) {
                Authentication auth = tokenProvider.getAuthentication(headerToken, request.getServletPath());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            filter.doFilter(request, response);
        } catch (Exception ex) {
            // Sends to GlobalExceptionHandler
            resolver.resolveException(request, response, null, ex);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/auth/signin");
    }
}