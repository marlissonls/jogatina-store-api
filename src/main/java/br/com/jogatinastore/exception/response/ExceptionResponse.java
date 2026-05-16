package br.com.jogatinastore.exception.response;

import java.time.OffsetDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ExceptionResponse")
public record ExceptionResponse(
    @Schema(example = "400")
    Number status,
    String code,
    String message,
    OffsetDateTime timestamp,
    List<ErrorDetail> errors
) {}