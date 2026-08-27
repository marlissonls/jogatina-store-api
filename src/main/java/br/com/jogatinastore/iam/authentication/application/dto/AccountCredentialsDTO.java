package br.com.jogatinastore.iam.authentication.application.dto;

import br.com.jogatinastore.iam.authentication.domain.exception.AuthErrors;
import jakarta.validation.constraints.*;

public record AccountCredentialsDTO(
    @NotBlank(message = AuthErrors.Code.AUTH_EMAIL_REQUIRED)
    @Email(message = AuthErrors.Code.AUTH_EMAIL_INVALID)
    String email,

    @NotBlank(message = AuthErrors.Code.AUTH_PASSWORD_REQUIRED)
    @Size(min = 8, message = AuthErrors.Code.AUTH_PASSWORD_SIZE)
    String password
) {}
