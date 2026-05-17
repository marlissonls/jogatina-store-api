package br.com.jogatinastore.security.dto;

import br.com.jogatinastore.exception.messages.AuthErrorCode;
import jakarta.validation.constraints.Pattern;

public record RefreshTokenDTO(

    @Pattern(
        regexp = "^Bearer .+$",
        message = AuthErrorCode.REFRESH_TOKEN_INVALID
    )
    String refreshToken
) {}
