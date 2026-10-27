package br.com.springecommerce.shared.exception.response;

public record ErrorDetail(
    String target,
    String code
) {}
