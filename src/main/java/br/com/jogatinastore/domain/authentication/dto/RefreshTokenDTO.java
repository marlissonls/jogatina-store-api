package br.com.jogatinastore.domain.authentication.dto;

import br.com.jogatinastore.domain.authentication.exception.AuthErrors;
import jakarta.validation.constraints.Pattern;

public record RefreshTokenDTO(

    @Pattern(
        regexp = "^Bearer .+$",
        message = AuthErrors.Code.REFRESH_TOKEN_INVALID
    )
    String refreshToken
) {}
