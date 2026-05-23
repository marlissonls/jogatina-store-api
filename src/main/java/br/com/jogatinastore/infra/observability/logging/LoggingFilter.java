package br.com.jogatinastore.infra.observability.logging;

import br.com.jogatinastore.infra.security.principal.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID = "correlationId";

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String correlationId = resolveCorrelationId(request);

            if (MDC.get(CORRELATION_ID) == null)
                MDC.put(CORRELATION_ID, correlationId);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            MDC.put("user", getUserEmail(auth));
            MDC.put("method", request.getMethod());
            MDC.put("path", request.getRequestURI());

            filterChain.doFilter(request, response);

            MDC.put("status", String.valueOf(response.getStatus()));
        } finally {
            MDC.remove(CORRELATION_ID);
            MDC.remove("user");
            MDC.remove("method");
            MDC.remove("path");
            MDC.remove("status");
        }
    }

    private String resolveCorrelationId(HttpServletRequest request) {
        String id = request.getHeader("X-Correlation-Id");
        return (id != null) ? id : UUID.randomUUID().toString();
    }

    private String getUserEmail(Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
            return "anonymous";

        Object principal = auth.getPrincipal();

        if (principal instanceof AuthenticatedUser user)
            return user.getEmail();

        return "anonymous";
    }
}