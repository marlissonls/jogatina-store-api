package br.com.jogatinastore.shared.exception.handler;

import br.com.jogatinastore.iam.role.domain.exception.RoleErrors;
import br.com.jogatinastore.shared.exception.code.ErrorCode;
import br.com.jogatinastore.shared.exception.order.ExceptionHandlerOrder;
import br.com.jogatinastore.shared.exception.response.ErrorDetail;
import br.com.jogatinastore.shared.exception.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
@Order(ExceptionHandlerOrder.AUTHORIZATION)
public class AuthorizationExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationExceptionHandler.class);

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {

        logger.warn("Security restriction: {}", ex.getMessage());

        var errors = List.of(
                new ErrorDetail(
                        RoleErrors.Target.PERMISSION,
                        RoleErrors.Code.PERMISSION_DENIED
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
