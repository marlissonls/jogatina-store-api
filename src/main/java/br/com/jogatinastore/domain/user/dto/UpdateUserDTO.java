package br.com.jogatinastore.domain.user.dto;

import br.com.jogatinastore.domain.user.exception.UserErrors;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserDTO(
        @NotNull(message = UserErrors.Code.USER_ID_REQUIRED)
        UUID id,

        @NotBlank(message = UserErrors.Code.USER_NAME_REQUIRED)
        @Size(min = 3, max = 100, message = UserErrors.Code.USER_NAME_SIZE)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]*$", message = UserErrors.Code.USER_NAME_INVALID_FORMAT)
        String name,

        @NotBlank(message = UserErrors.Code.USER_PHONE_REQUIRED)
        @Pattern(regexp = "\\d{10,13}", message = UserErrors.Code.USER_PHONE_INVALID)
        String phoneNumber,

        @NotNull(message = UserErrors.Code.USER_BIRTHDATE_REQUIRED)
        @Past(message = UserErrors.Code.USER_BIRTHDATE_PAST_REQUIRED)
        LocalDate birthDate
) {}
