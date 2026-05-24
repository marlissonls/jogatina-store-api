package br.com.jogatinastore.infra.exception.handler;

import br.com.jogatinastore.domain.authorization.exception.AuthorizationErrors;
import br.com.jogatinastore.infra.exception.code.ErrorCode;
import br.com.jogatinastore.infra.exception.response.ErrorDetail;
import br.com.jogatinastore.infra.exception.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class AuthorizationExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationExceptionHandler.class);

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {

        logger.warn("Security restriction: {}", ex.getMessage());

        var errors = List.of(
                new ErrorDetail(
                        AuthorizationErrors.Target.AUTHZ,
                        AuthorizationErrors.Code.ACCESS_DENIED
                )
        );

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.FORBIDDEN.value(),
                ErrorCode.NOT_AUTHORIZED.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                errors
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
