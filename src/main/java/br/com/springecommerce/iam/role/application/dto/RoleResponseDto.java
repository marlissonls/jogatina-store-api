package br.com.springecommerce.iam.role.application.dto;

import br.com.springecommerce.iam.role.domain.model.Role;

import java.util.UUID;

public record RoleResponseDto(
        UUID id,
        String title,
        String description
) {
    public RoleResponseDto(Role role) {
        this(
                role.getId(),
                role.getTitle(),
                role.getDescription()
        );
    }
}
