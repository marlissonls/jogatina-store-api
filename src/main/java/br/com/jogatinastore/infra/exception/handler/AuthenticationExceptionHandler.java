package br.com.jogatinastore.infra.exception.handler;

import br.com.jogatinastore.domain.authentication.exception.AuthErrors;
import br.com.jogatinastore.infra.exception.InvalidJwtTokenException;
import br.com.jogatinastore.infra.exception.code.ErrorCode;
import br.com.jogatinastore.infra.exception.response.ErrorDetail;
import br.com.jogatinastore.infra.exception.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {

        logger.warn("Authentication failed: {}", ex.getMessage());

        var errors = List.of(
            new ErrorDetail(
                AuthErrors.Target.CREDENTIALS,
                AuthErrors.Code.CREDENTIALS_INVALID
            )
        );

        ExceptionResponse response = new ExceptionResponse(
            HttpStatus.UNAUTHORIZED.value(),
            ErrorCode.BAD_CREDENTIALS.name(),
            ex.getMessage(),
            OffsetDateTime.now(),
            errors
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtTokenException(InvalidJwtTokenException ex) {

        logger.warn("Authentication failed: {}", ex.getMessage());

        ExceptionResponse response = new ExceptionResponse(
            HttpStatus.UNAUTHORIZED.value(),
            ErrorCode.INVALID_TOKEN.name(),
            ex.getMessage(),
            OffsetDateTime.now(),
            ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
