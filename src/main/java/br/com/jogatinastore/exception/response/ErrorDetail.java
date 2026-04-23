package br.com.jogatinastore.exception.response;

public record ErrorDetail(
    String target,
    String code
) {}
