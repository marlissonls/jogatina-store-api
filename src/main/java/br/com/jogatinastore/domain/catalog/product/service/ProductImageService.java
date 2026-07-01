package br.com.jogatinastore.domain.catalog.product.service;

import br.com.jogatinastore.domain.catalog.product.repository.ProductImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {

    private final ProductImageRepository repository;

    public ProductImageService(ProductImageRepository repository) {
        this.repository = repository;
    }
}
