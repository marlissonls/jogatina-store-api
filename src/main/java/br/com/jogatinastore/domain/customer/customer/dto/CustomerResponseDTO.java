package br.com.jogatinastore.domain.customer.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Schema(name = "CustomerResponseDTO")
public record CustomerResponseDTO(
    UUID id,
    String name,
    String cpf,
    String phone,
    LocalDate birthDate
) {}
