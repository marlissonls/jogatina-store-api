package br.com.jogatinastore.sales.cart.domain.exception;

import br.com.jogatinastore.shared.exception.base.ApplicationException;
import br.com.jogatinastore.shared.exception.response.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartIsEmptyException extends ApplicationException {

    public CartIsEmptyException(String target, String code) {
        super("Cart is empty",
                List.of(new ErrorDetail(target, code))
        );
    }
}
