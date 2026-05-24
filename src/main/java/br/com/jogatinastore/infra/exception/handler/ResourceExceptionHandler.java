package br.com.jogatinastore.infra.exception.handler;

import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
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
public class ResourceExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResourceExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {

        logger.warn("Resource not found. Errors={}", ex.getErrors());

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.NOT_FOUND.value(),
                ErrorCode.NOT_FOUND.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //AlreadyExistsError
}