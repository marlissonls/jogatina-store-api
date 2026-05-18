package br.com.jogatinastore.infra.security.dto;

import br.com.jogatinastore.domain.user.exception.UserErrorCode;
import jakarta.validation.constraints.*;

public record AccountCredentialsDTO(

    @NotBlank(message = UserErrorCode.USER_EMAIL_REQUIRED)
    @Email(message = UserErrorCode.USER_EMAIL_INVALID)
    String email,

    @NotBlank(message = UserErrorCode.USER_PASSWORD_REQUIRED)
    @Size(min = 8, message = UserErrorCode.USER_PASSWORD_SIZE)
    String password
) {}
