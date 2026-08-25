package br.com.jogatinastore.domain.iam.role.dto;

import java.util.UUID;

public record RoleUpdateDTO(
        UUID id,
        String title,
        String description
) {}