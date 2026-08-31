package br.com.jogatinastore.iam.user.application.dto;

import br.com.jogatinastore.iam.user.domain.exception.UserErrors;
import jakarta.validation.constraints.*;

public record UserCreateDto(
        @NotBlank(message = UserErrors.Code.USER_EMAIL_REQUIRED)
        @Email(message = UserErrors.Code.USER_EMAIL_INVALID)
        String email,

        @NotBlank(message = UserErrors.Code.USER_PASSWORD_REQUIRED)
        @Size(min = 8, message = UserErrors.Code.USER_PASSWORD_SIZE)
        String password
) {}
