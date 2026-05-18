package br.com.jogatinastore.shared;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "PageResponse")
public record PageResponse<T>(
    List<T> items,
    int page,
    int size,
    long total
) {}