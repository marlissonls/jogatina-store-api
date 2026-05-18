package br.com.jogatinastore.infra.exception;

import br.com.jogatinastore.infra.exception.response.ErrorDetail;

import java.util.List;

public class InvalidJwtTokenException extends ApplicationException {

    public InvalidJwtTokenException(String target, String code) {

        super("Token expired or is invalid", List.of(new ErrorDetail(target, code)));
    }
}
