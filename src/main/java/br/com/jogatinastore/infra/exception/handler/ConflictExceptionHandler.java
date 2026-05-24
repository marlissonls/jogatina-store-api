package br.com.jogatinastore.infra.exception.handler;

import br.com.jogatinastore.infra.exception.ConflictException;
import br.com.jogatinastore.infra.exception.code.ErrorCode;
import br.com.jogatinastore.infra.exception.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ConflictExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConflictExceptionHandler.class);

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> handleConflictException(ConflictException ex) {

        logger.warn("Conflict occurred. Errors={}", ex.getErrors());

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.CONFLICT.value(),
                ErrorCode.ALREADY_EXISTS.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
