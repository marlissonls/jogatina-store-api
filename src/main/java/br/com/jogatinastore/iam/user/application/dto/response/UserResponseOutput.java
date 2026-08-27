package br.com.jogatinastore.iam.user.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Schema(name = "UserResponseDTO")
public record UserResponseOutput(
    UUID id,
    String email,
    Boolean enabled,
    Set<String> roles,
    LocalDateTime createdAt
) {}
