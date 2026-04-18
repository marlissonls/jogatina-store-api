package br.com.jogatinastore.exception.handler;

import br.com.jogatinastore.exception.ResourceNotFoundException;
import br.com.jogatinastore.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(500).body(
            new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Algo deu errado",
                OffsetDateTime.now()
            )
        );
    }
}
