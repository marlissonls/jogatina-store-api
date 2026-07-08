package br.com.jogatinastore.domain.catalog.product.dto;

import br.com.jogatinastore.domain.catalog.product.entity.Product;
import br.com.jogatinastore.domain.inventory.stock.entity.Stock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductWithStockResponseDTO(
        UUID id,
        String title,
        String slug,
        String barcode,
        String sku,
        String description,
        BigDecimal price,
        BigDecimal salePrice,
        BigDecimal costPrice,
        Integer stock,
        Integer reservedStock,
        Integer minimumStock,
        BigDecimal rating,
        Integer ratingCount,
        Boolean active,
        Boolean featured,
        UUID categoryId,
        UUID brandId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ProductWithStockResponseDTO(Product product, Stock stock) {
        this(
            product.getId(),
            product.getTitle(),
            product.getSlug(),
            product.getBarcode(),
            product.getSku(),
            product.getDescription(),
            product.getPrice(),
            product.getSalePrice(),
            product.getCostPrice(),
            stock.getAvailableQuantity(),
            stock.getReservedQuantity(),
            stock.getMinimumQuantity(),
            product.getRating(),
            product.getRatingCount(),
            product.getActive(),
            product.getFeatured(),
            product.getCategoryId(),
            product.getBrandId(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}