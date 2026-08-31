package br.com.jogatinastore.iam.role.application.dto;

import java.util.UUID;

public record RoleUpdateDto(
        UUID id,
        String title,
        String description
) {}