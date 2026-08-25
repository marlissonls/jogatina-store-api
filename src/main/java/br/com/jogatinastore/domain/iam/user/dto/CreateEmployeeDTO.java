package br.com.jogatinastore.domain.iam.user.dto;

import br.com.jogatinastore.domain.iam.user.exception.UserErrors;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record CreateEmployeeDTO(
        @NotBlank(message = UserErrors.Code.USER_EMAIL_REQUIRED)
        @Email(message = UserErrors.Code.USER_EMAIL_INVALID)
        String email,

        @NotBlank(message = UserErrors.Code.USER_PASSWORD_REQUIRED)
        @Size(min = 8, message = UserErrors.Code.USER_PASSWORD_SIZE)
        String password,

        @NotEmpty(message = UserErrors.Code.USER_ROLE_IDS_REQUIRED)
        List<@NotNull(message = UserErrors.Code.USER_ROLE_ID_INVALID) UUID> roleIds
) {}
