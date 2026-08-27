package br.com.jogatinastore.config.security.jwt.exception;

import br.com.jogatinastore.shared.exception.base.ApplicationException;
import br.com.jogatinastore.shared.exception.response.ErrorDetail;

import java.util.List;

public class InvalidJwtTokenException extends ApplicationException {

    public InvalidJwtTokenException(String target, String code) {

        super("Token expired or is invalid", List.of(new ErrorDetail(target, code)));
    }
}
