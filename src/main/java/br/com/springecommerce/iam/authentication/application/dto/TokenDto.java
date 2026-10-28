package br.com.springecommerce.iam.authentication.application.dto;

import java.util.Date;

public record TokenDto(
   String email,
   Boolean authenticated,
   Date created,
   Date expiration,
   String accessToken,
   String refreshToken
) {}
