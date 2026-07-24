package br.com.jogatinastore.infra.exception.handler;

import br.com.jogatinastore.infra.exception.code.ErrorCode;
import br.com.jogatinastore.infra.exception.errors.SystemErrors;
import br.com.jogatinastore.infra.exception.handler.order.ExceptionHandlerOrder;
import br.com.jogatinastore.infra.exception.response.ErrorDetail;
import br.com.jogatinastore.infra.exception.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
@Order(ExceptionHandlerOrder.INFRASTRUCTURE)
public class InfrastructureExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(InfrastructureExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleInfraestructureExceptions(Exception ex) {

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
}
