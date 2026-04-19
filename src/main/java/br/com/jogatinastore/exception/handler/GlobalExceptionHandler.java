package br.com.jogatinastore.exception.handler;

import br.com.jogatinastore.exception.ConflictException;
import br.com.jogatinastore.exception.ResourceNotFoundException;
import br.com.jogatinastore.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(MethodArgumentNotValidException ex) {

        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "Falha nos dados do usuário. Campos inválidos: " + details,
                OffsetDateTime.now()
        );

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFound(Exception ex) {
        return ResponseEntity.status(400).body(
            new ExceptionResponse(
                HttpStatus.NOT_FOUND.toString(),
                ex.getMessage(),
                OffsetDateTime.now()
            )
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> handleConflict(Exception ex) {
        return ResponseEntity.status(409).body(
            new ExceptionResponse(
                HttpStatus.CONFLICT.toString(),
                ex.getMessage(),
                OffsetDateTime.now()
            )
        );
    }
}
