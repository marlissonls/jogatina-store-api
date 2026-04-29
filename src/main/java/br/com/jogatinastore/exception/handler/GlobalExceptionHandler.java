package br.com.jogatinastore.exception.handler;

import br.com.jogatinastore.exception.ConflictException;
import br.com.jogatinastore.exception.InvalidJwtTokenException;
import br.com.jogatinastore.exception.ResourceNotFoundException;
import br.com.jogatinastore.exception.messages.AuthErrorCode;
import br.com.jogatinastore.exception.messages.AuthErrorTarget;
import br.com.jogatinastore.exception.messages.ErrorType;
import br.com.jogatinastore.exception.response.ExceptionResponse;
import br.com.jogatinastore.exception.response.ErrorDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex) {

        logger.error("Unexpected error occurred. Errors={}", ex.getMessage(), ex);

        var errors = List.of(new ErrorDetail("system", "error.internal"));

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ErrorType.INTERNAL_ERROR.name(),
                "Unexpected error",
                OffsetDateTime.now(),
                errors
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {

        String parameterName = ex.getName();
        String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String receivedType = ex.getValue() != null ? ex.getValue().toString() : "unknown";
        logger.warn("Url parameter error. Parameter '{}' expects type '{}' but received '{}'", parameterName, expectedType, receivedType);

        var errors = List.of(new ErrorDetail("client.path_parameter." + parameterName, "error.client.path_parameter.invalid"));

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ErrorType.INVALID_PATH_PARAMETER.name(),
                "Invalid path parameter",
                OffsetDateTime.now(),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        Throwable rootCause = ex.getMostSpecificCause();
        String detailedMessage = rootCause.getMessage();
        logger.warn("Fail to read JSON: {}", detailedMessage);

        String userMessage = "JSON formatting error or invalid data type.";
        if (rootCause.toString().contains("UUID"))
            userMessage = "UUID invalid format.";
        else if (rootCause.toString().contains("Date") || detailedMessage.contains("DateTime"))
            userMessage = "Data/Hour invalid format.";

        var errors = List.of(new ErrorDetail("client.payload", "error.client.payload.invalid_json"));

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ErrorType.INVALID_JSON_BODY.name(),
                userMessage,
                OffsetDateTime.now(),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<ErrorDetail> errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorDetail(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        logger.warn("Validation failed. Fields={}", errorDetails);

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ErrorType.VALIDATION_ERROR.name(),
                "Validation failed",
                OffsetDateTime.now(),
                errorDetails
        );

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {

        logger.warn("Resource not found. Errors={}", ex.getErrors());

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.NOT_FOUND.value(),
                ErrorType.NOT_FOUND.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> handleConflictException(ConflictException ex) {

        logger.warn("Conflict occurred. Errors={}", ex.getErrors());

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.CONFLICT.value(),
                ErrorType.BUSINESS_ERROR.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {

        logger.warn("Authentication failed: {}", ex.getMessage());

        var errors = List.of(new ErrorDetail(AuthErrorTarget.CREDENTIALS, AuthErrorCode.CREDENTIALS_INVALID));

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ErrorType.BAD_CREDENTIALS.name(),
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
                ErrorType.INVALID_TOKEN.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
