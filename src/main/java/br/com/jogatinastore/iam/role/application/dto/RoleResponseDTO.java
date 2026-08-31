package br.com.jogatinastore.iam.role.application.dto;

import br.com.jogatinastore.iam.role.domain.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "RoleResponseDTO")
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
