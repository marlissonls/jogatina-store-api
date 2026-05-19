package br.com.jogatinastore.domain.user.dto;

import br.com.jogatinastore.domain.user.exception.UserErrors;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;

public record CreateUserDTO(
        @NotBlank(message = UserErrors.Code.USER_NAME_REQUIRED)
        @Size(min = 3, max = 100, message = UserErrors.Code.USER_NAME_SIZE)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]*$", message = UserErrors.Code.USER_NAME_INVALID_FORMAT)
        String name,

        @NotBlank(message = UserErrors.Code.USER_CPF_REQUIRED)
        @CPF(message = UserErrors.Code.USER_CPF_INVALID) // CPF inválido (formato)
        String cpf,

        @NotNull(message = UserErrors.Code.USER_BIRTHDATE_REQUIRED)
        @Past(message = UserErrors.Code.USER_BIRTHDATE_PAST_REQUIRED)
        LocalDate birthDate,

        @NotBlank(message = UserErrors.Code.USER_PHONE_REQUIRED)
        @Pattern(regexp = "\\d{10,13}", message = UserErrors.Code.USER_PHONE_INVALID)
        String phoneNumber,

        @NotBlank(message = UserErrors.Code.USER_EMAIL_REQUIRED)
        @Email(message = UserErrors.Code.USER_EMAIL_INVALID)
        String email,

        @NotBlank(message = UserErrors.Code.USER_PASSWORD_REQUIRED)
        @Size(min = 8, message = UserErrors.Code.USER_PASSWORD_SIZE)
        String password
) {}
