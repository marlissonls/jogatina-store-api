package br.com.jogatinastore.infra.exception.handler;

import br.com.jogatinastore.infra.exception.*;
import br.com.jogatinastore.infra.exception.code.ErrorCode;
import br.com.jogatinastore.infra.exception.handler.order.ExceptionHandlerOrder;
import br.com.jogatinastore.infra.exception.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
@Order(ExceptionHandlerOrder.CONFLICT)
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

    @ExceptionHandler(InsufficientStockException.class)
    public final ResponseEntity<ExceptionResponse> handleInsufficientStockException(InsufficientStockException ex) {

        logger.warn("Cart Item unavailable. Errors={}", ex.getErrors());

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.CONFLICT.value(),
                ErrorCode.INSUFFICIENT_STOCK.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(CartItemUnavailableException.class)
    public final ResponseEntity<ExceptionResponse> handleCartItemUnavailableException(CartItemUnavailableException ex) {

        logger.warn("Cart Item unavailable. Errors={}", ex.getErrors());

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.CONFLICT.value(),
                ErrorCode.ITEM_UNAVAILABLE.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(CannotRemoveLastRoleException.class)
    public final ResponseEntity<ExceptionResponse> handleCannotRemoveLastRoleException(CannotRemoveLastRoleException ex) {

        logger.warn("Cannot remove last role. Errors={}", ex.getErrors());

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.CONFLICT.value(),
                ErrorCode.CANNOT_REMOVE_LAST_ROLE.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(RoleNotAssignedException.class)
    public final ResponseEntity<ExceptionResponse> handleRoleNotAssignedException(RoleNotAssignedException ex) {

        logger.warn("Role is not assigned. Errors={}", ex.getErrors());

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.CONFLICT.value(),
                ErrorCode.ROLE_NOT_ASSIGNED.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
