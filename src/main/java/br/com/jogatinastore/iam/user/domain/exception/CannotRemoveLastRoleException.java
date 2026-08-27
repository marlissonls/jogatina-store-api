package br.com.jogatinastore.iam.user.domain.exception;

import br.com.jogatinastore.shared.exception.base.ApplicationException;
import br.com.jogatinastore.shared.exception.response.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CannotRemoveLastRoleException extends ApplicationException {

    public CannotRemoveLastRoleException(String target, String code) {
        super("Can not remove last role",
            List.of(new ErrorDetail(target, code))
        );
    }
}
