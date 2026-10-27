package br.com.springecommerce.catalog.product.domain.exception;

import br.com.springecommerce.shared.exception.base.ApplicationException;
import br.com.springecommerce.shared.exception.response.ErrorDetail;

import java.util.List;

public class ProductUnavailableException extends ApplicationException {
    public ProductUnavailableException(String target, String code) {
        super("Product unavailable",
                List.of(new ErrorDetail(target, code))
        );
    }
}
