package br.com.jogatinastore.iam.role.application.dto;

import br.com.jogatinastore.iam.role.domain.model.Role;

import java.util.UUID;

public record RoleResponseDTO(
        UUID id,
        String title,
        String description
) {
    public RoleResponseDTO(Role role) {
        this(
                role.getId(),
                role.getTitle(),
                role.getDescription()
        );
    }
}
