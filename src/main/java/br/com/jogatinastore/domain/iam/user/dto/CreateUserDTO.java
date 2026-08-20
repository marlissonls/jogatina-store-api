package br.com.jogatinastore.domain.iam.user.dto;

import br.com.jogatinastore.domain.iam.user.exception.UserErrors;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;

public record CreateUserDTO(
        @NotBlank(message = UserErrors.Code.USER_EMAIL_REQUIRED)
        @Email(message = UserErrors.Code.USER_EMAIL_INVALID)
        String email,

        @NotBlank(message = UserErrors.Code.USER_PASSWORD_REQUIRED)
        @Size(min = 8, message = UserErrors.Code.USER_PASSWORD_SIZE)
        String password
) {}
