package br.com.springecommerce.customer.customer.application.dto;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerResponseDto(
    UUID id,
    String name,
    String cpf,
    String phone,
    LocalDate birthDate
) {}
