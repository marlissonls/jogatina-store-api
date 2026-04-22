package br.com.jogatinastore.model.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

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
