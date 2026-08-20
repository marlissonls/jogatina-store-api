package br.com.jogatinastore.domain.customer.customer.dto;

import br.com.jogatinastore.domain.customer.customer.exception.CustomerErrors;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CustomerCreateDTO(
        @NotBlank(message = CustomerErrors.Code.CUSTOMER_NAME_REQUIRED)
        @Size(min = 3, max = 100, message = CustomerErrors.Code.CUSTOMER_NAME_SIZE)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]*$", message = CustomerErrors.Code.CUSTOMER_NAME_INVALID_FORMAT)
        String name,

        @NotBlank(message = CustomerErrors.Code.CUSTOMER_CPF_REQUIRED)
        @CPF(message = CustomerErrors.Code.CUSTOMER_CPF_INVALID)
        String cpf,

        @NotNull(message = CustomerErrors.Code.CUSTOMER_BIRTHDATE_REQUIRED)
        @Past(message = CustomerErrors.Code.CUSTOMER_BIRTHDATE_PAST_REQUIRED)
        LocalDate birthDate,

        @NotBlank(message = CustomerErrors.Code.CUSTOMER_PHONE_REQUIRED)
        @Pattern(regexp = "\\d{10,13}", message = CustomerErrors.Code.CUSTOMER_PHONE_INVALID)
        String phone
) {}
