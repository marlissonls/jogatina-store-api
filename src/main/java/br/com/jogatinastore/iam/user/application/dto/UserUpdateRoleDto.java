package br.com.jogatinastore.iam.user.application.dto;

import br.com.jogatinastore.iam.user.domain.exception.UserErrors;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserUpdateRoleDto(
        @NotNull(message = UserErrors.Code.USER_ID_INVALID)
        UUID userId,

        @NotNull(message = UserErrors.Code.USER_ROLE_ID_INVALID)
        UUID roleId
) {}
