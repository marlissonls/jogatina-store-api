package br.com.jogatinastore.domain.iam.role.dto;

import br.com.jogatinastore.domain.iam.role.entity.Role;

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
