package br.com.jogatinastore.infra.exception.response;

public record ErrorDetail(
    String target,
    String code
) {}
