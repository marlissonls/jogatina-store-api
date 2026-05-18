package br.com.jogatinastore.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Schema(name = "UserResponseDTO")
public record UserResponseDTO(
    UUID id,
    String name,
    String email,
    String cpf,
    String phoneNumber,
    LocalDate birthDate,
    Boolean enabled,
    Set<String> permissions,
    LocalDateTime createdAt
) {}
