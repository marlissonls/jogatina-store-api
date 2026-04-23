package br.com.jogatinastore.model.user.dto;

import br.com.jogatinastore.exception.messages.UserErrorCode;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserDTO(
        @NotNull(message = UserErrorCode.USER_ID_REQUIRED)
        UUID id,

        @NotBlank(message = UserErrorCode.USER_NAME_REQUIRED)
        @Size(min = 3, max = 100, message = UserErrorCode.USER_NAME_SIZE)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]*$", message = UserErrorCode.USER_NAME_INVALID_FORMAT)
        String name,

        @NotBlank(message = UserErrorCode.USER_PHONE_REQUIRED)
        @Pattern(regexp = "\\d{10,13}", message = UserErrorCode.USER_PHONE_INVALID)
        String phoneNumber,

        @NotNull(message = UserErrorCode.USER_BIRTHDATE_REQUIRED)
        @Past(message = UserErrorCode.USER_BIRTHDATE_PAST_REQUIRED)
        LocalDate birthDate
) {}
