package br.com.jogatinastore.domain.iam.user.dto;

import br.com.jogatinastore.domain.iam.user.exception.UserErrors;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateUserRoleDTO(
        @NotNull(message = UserErrors.Code.USER_ID_INVALID)
        UUID userId,

        @NotNull(message = UserErrors.Code.USER_ROLE_ID_INVALID)
        UUID roleId
) {}
