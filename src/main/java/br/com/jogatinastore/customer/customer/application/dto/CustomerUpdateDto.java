package br.com.jogatinastore.customer.customer.application.dto;

import br.com.jogatinastore.customer.customer.domain.exception.CustomerErrors;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerUpdateDto(
        @NotNull(message = CustomerErrors.Code.CUSTOMER_ID_REQUIRED)
        UUID id,

        @NotBlank(message = CustomerErrors.Code.CUSTOMER_NAME_REQUIRED)
        @Size(min = 3, max = 100, message = CustomerErrors.Code.CUSTOMER_NAME_SIZE)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]*$", message = CustomerErrors.Code.CUSTOMER_NAME_INVALID_FORMAT)
        String name,

        @NotBlank(message = CustomerErrors.Code.CUSTOMER_PHONE_REQUIRED)
        @Pattern(regexp = "\\d{10,13}", message = CustomerErrors.Code.CUSTOMER_PHONE_INVALID)
        String phone,

        @NotNull(message = CustomerErrors.Code.CUSTOMER_BIRTHDATE_REQUIRED)
        @Past(message = CustomerErrors.Code.CUSTOMER_BIRTHDATE_PAST_REQUIRED)
        LocalDate birthDate
) {}
