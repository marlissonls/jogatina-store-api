package br.com.jogatinastore.infra.exception;

import br.com.jogatinastore.infra.exception.response.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotAssignedException extends ApplicationException {

    public RoleNotAssignedException(String target, String code) {
        super("Role not assigned",
            List.of(new ErrorDetail(target, code))
        );
    }
}
