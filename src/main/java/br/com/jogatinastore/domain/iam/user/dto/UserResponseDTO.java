package br.com.jogatinastore.domain.iam.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Schema(name = "UserResponseDTO")
public record UserResponseDTO(
    UUID id,
    String email,
    Boolean enabled,
    Set<String> roles,
    LocalDateTime createdAt
) {}
