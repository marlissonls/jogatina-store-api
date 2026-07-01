package br.com.jogatinastore.domain.catalog.product.dto;

import br.com.jogatinastore.domain.catalog.product.entity.Product;
import br.com.jogatinastore.domain.catalog.product.entity.ProductImage;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String title,
        String description,
        BigDecimal price,
        BigDecimal salePrice,
        BigDecimal rating,
        List<String> images
) {
    public ProductResponseDTO(Product product) {
        this(
            product.getId(),
            product.getTitle(),
            product.getDescription(),
            product.getPrice(),
            product.getSalePrice(),
            product.getRating(),
            product
                .getProductImages()
                .stream()
                .map(ProductImage::getUrl)
                .toList()
        );
    }
}