package br.com.jogatinastore.inventory.stock.domain.exception;

import br.com.jogatinastore.shared.exception.base.ApplicationException;
import br.com.jogatinastore.shared.exception.response.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.CONFLICT)
public class InsufficientStockException extends ApplicationException {

    public InsufficientStockException(String target, String code) {
        super("Not enough stock available",
            List.of(new ErrorDetail(target, code))
        );
    }
}
