package br.com.jogatinastore.model.user.dto;

import br.com.jogatinastore.exception.messages.UserErrorCode;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;

public record CreateUserDTO(
        @NotBlank(message = UserErrorCode.USER_NAME_REQUIRED)
        @Size(min = 3, max = 100, message = UserErrorCode.USER_NAME_SIZE)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]*$", message = UserErrorCode.USER_NAME_INVALID_FORMAT)
        String name,

        @NotBlank(message = UserErrorCode.USER_CPF_REQUIRED)
        @CPF(message = UserErrorCode.USER_CPF_INVALID) // CPF inválido (formato)
        String cpf,

        @NotNull(message = UserErrorCode.USER_BIRTHDATE_REQUIRED)
        @Past(message = UserErrorCode.USER_BIRTHDATE_PAST_REQUIRED)
        LocalDate birthDate,

        @NotBlank(message = UserErrorCode.USER_PHONE_REQUIRED)
        @Pattern(regexp = "\\d{10,13}", message = UserErrorCode.USER_PHONE_INVALID)
        String phoneNumber,

        @NotBlank(message = UserErrorCode.USER_EMAIL_REQUIRED)
        @Email(message = UserErrorCode.USER_EMAIL_INVALID)
        String email,

        @NotBlank(message = UserErrorCode.USER_PASSWORD_REQUIRED)
        @Size(min = 8, message = UserErrorCode.USER_PASSWORD_SIZE)
        String password
) {}
