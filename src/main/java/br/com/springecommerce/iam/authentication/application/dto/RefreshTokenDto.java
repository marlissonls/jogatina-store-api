package br.com.springecommerce.iam.authentication.application.dto;

import br.com.springecommerce.iam.authentication.domain.exception.AuthErrors;
import jakarta.validation.constraints.Pattern;

public record RefreshTokenDto(

    @Pattern(
        regexp = "^Bearer .+$",
        message = AuthErrors.Code.REFRESH_TOKEN_INVALID
    )
    String refreshToken
) {}
