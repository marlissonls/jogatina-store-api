package br.com.jogatinastore.iam.authentication.application.dto;

import br.com.jogatinastore.iam.authentication.domain.exception.AuthErrors;
import jakarta.validation.constraints.Pattern;

public record RefreshTokenDto(

    @Pattern(
        regexp = "^Bearer .+$",
        message = AuthErrors.Code.REFRESH_TOKEN_INVALID
    )
    String refreshToken
) {}
