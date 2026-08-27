package br.com.jogatinastore.iam.user.application.dto.request;

import br.com.jogatinastore.iam.user.domain.exception.UserErrors;
import jakarta.validation.constraints.*;

public record CreateUserInput(
        @NotBlank(message = UserErrors.Code.USER_EMAIL_REQUIRED)
        @Email(message = UserErrors.Code.USER_EMAIL_INVALID)
        String email,

        @NotBlank(message = UserErrors.Code.USER_PASSWORD_REQUIRED)
        @Size(min = 8, message = UserErrors.Code.USER_PASSWORD_SIZE)
        String password
) {}
