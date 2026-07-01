package br.com.jogatinastore.domain.catalog.review.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReviewResponseDTO(
    String username,
    BigDecimal rating,
    String comment,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}