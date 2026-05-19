package br.com.jogatinastore.infra.exception.handler;

import br.com.jogatinastore.infra.exception.ConflictException;
import br.com.jogatinastore.infra.exception.InvalidJwtTokenException;
import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
import br.com.jogatinastore.domain.authentication.exception.AuthErrors;
import br.com.jogatinastore.domain.authorization.exception.AuthorizationErrors;
import br.com.jogatinastore.infra.exception.code.ErrorCode;
import br.com.jogatinastore.infra.exception.errors.RequestErrors;
import br.com.jogatinastore.infra.exception.errors.SystemErrors;
import br.com.jogatinastore.infra.exception.response.ExceptionResponse;
import br.com.jogatinastore.infra.exception.response.ErrorDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
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

        var errors = List.of(
            new ErrorDetail(
                SystemErrors.Target.SYSTEM,
                SystemErrors.Code.INTERNAL
            )
        );

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ErrorCode.INTERNAL_ERROR.name(),
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

        var errors = List.of(
            new ErrorDetail(
                RequestErrors.Target.PATH_PARAMETER + "." + parameterName,
                RequestErrors.Code.INVALID_PATH_PARAMETER
            )
        );

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ErrorCode.INVALID_PATH_PARAMETER.name(),
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
        String field = "";
        if (rootCause.toString().contains("UUID")) {
            userMessage = "UUID invalid format.";
            field = "." + "id";
        }
        else if (rootCause.toString().contains("Date") || detailedMessage.contains("DateTime")) {
            userMessage = "Data/Hour invalid format.";
            field = "." + "birthDate";
        }

        var errors = List.of(
            new ErrorDetail(
                RequestErrors.Target.PAYLOAD + field,
                RequestErrors.Code.INVALID_PAYLOAD
            )
        );

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ErrorCode.INVALID_JSON_BODY.name(),
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
                .map(error -> {
                    String code = error.getDefaultMessage();
                    String field = error.getField();

                    String target = extractTarget(code, field);

                    return new ErrorDetail(target, code);
                })
                .toList();

        logger.warn("Validation failed. Fields={}", errorDetails);

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ErrorCode.VALIDATION_ERROR.name(),
                "Validation failed",
                OffsetDateTime.now(),
                errorDetails
        );

        return ResponseEntity.status(400).body(response);
    }

    private String extractTarget(String code, String field) {
        if (code == null || !code.startsWith("error."))
            return field;

        String[] parts = code.split("\\.");

        if (parts.length >= 3)
            return parts[1] + "." + field;
        return field;
    }

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

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> handleConflictException(ConflictException ex) {

        logger.warn("Conflict occurred. Errors={}", ex.getErrors());

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.CONFLICT.value(),
                ErrorCode.BUSINESS_ERROR.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

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
