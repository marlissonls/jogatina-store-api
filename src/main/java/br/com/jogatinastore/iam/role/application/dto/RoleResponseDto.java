package br.com.jogatinastore.iam.role.application.dto;

import br.com.jogatinastore.iam.role.domain.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "RoleResponseDto")
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
