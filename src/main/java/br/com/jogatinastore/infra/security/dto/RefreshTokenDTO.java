package br.com.jogatinastore.infra.security.dto;

import br.com.jogatinastore.domain.authentication.exception.AuthErrorCode;
import jakarta.validation.constraints.Pattern;

public record RefreshTokenDTO(

    @Pattern(
        regexp = "^Bearer .+$",
        message = AuthErrorCode.REFRESH_TOKEN_INVALID
    )
    String refreshToken
) {}
