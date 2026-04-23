package br.com.jogatinastore.exception.response;

import java.time.OffsetDateTime;
import java.util.List;

public record ExceptionResponse(
    Number status,
    String code,
    String message,
    OffsetDateTime timestamp,
    List<ErrorDetail> errors
) {}
