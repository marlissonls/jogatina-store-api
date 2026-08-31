package br.com.jogatinastore.customer.customer.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "CustomerResponseDto")
public record CustomerResponseDto(
    UUID id,
    String name,
    String cpf,
    String phone,
    LocalDate birthDate
) {}
