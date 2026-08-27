package br.com.jogatinastore.iam.role.application.dto;

import java.util.UUID;

public record RoleUpdateDTO(
        UUID id,
        String title,
        String description
) {}