package br.com.jogatinastore.exception;

import br.com.jogatinastore.exception.response.ErrorDetail;

import java.util.List;

public abstract class ApplicationException extends RuntimeException {

    private final List<ErrorDetail> errors;

    public ApplicationException(String message, List<ErrorDetail> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ErrorDetail> getErrors() {
        return errors;
    }
}
