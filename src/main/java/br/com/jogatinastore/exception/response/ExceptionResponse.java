package br.com.jogatinastore.exception.response;

import java.time.OffsetDateTime;

public record ExceptionResponse(
    String code,
    String message,
    OffsetDateTime timestamp
) {}
