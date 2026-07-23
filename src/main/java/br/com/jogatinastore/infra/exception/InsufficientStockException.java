package br.com.jogatinastore.infra.exception;

import br.com.jogatinastore.infra.exception.response.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.CONFLICT)
public class InsufficientStockException extends ApplicationException {

    public InsufficientStockException(String target, String code) {
        super("Insufficient stock",
            List.of(new ErrorDetail(target, code))
        );
    }
}
