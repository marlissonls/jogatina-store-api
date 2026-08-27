package br.com.jogatinastore.shared.exception.response;

public record ErrorDetail(
    String target,
    String code
) {}
