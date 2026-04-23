package br.com.jogatinastore.exception;

import br.com.jogatinastore.exception.response.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String target, String code) {
        super("Resource not found",
            List.of(new ErrorDetail(target, code))
        );
    }
}
