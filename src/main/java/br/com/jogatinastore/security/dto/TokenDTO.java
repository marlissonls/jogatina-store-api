package br.com.jogatinastore.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "TokenDTO")
public record TokenDTO(
   String email,
   Boolean authenticated,
   Date created,
   Date expiration,
   String accessToken,
   String refreshToken
) {}
