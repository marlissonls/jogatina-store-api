package br.com.springecommerce.iam.user.application.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponseDto(
    UUID id,
    String email,
    Boolean enabled,
    Set<String> roles,
    LocalDateTime createdAt
) {}
