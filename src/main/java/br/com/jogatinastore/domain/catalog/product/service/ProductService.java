package br.com.jogatinastore.domain.catalog.product.service;

import br.com.jogatinastore.domain.catalog.brand.service.BrandService;
import br.com.jogatinastore.domain.catalog.category.service.CategoryService;
import br.com.jogatinastore.domain.catalog.product.dto.ProductRequestDTO;
import br.com.jogatinastore.domain.catalog.product.dto.ProductResponseDTO;
import br.com.jogatinastore.domain.catalog.product.entity.Product;
import br.com.jogatinastore.domain.catalog.product.exception.ProductErrors;
import br.com.jogatinastore.domain.catalog.product.repository.ProductRepository;
import br.com.jogatinastore.domain.catalog.product.specifications.ProductSpecifications;
import br.com.jogatinastore.infra.exception.ConflictException;
import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
import br.com.jogatinastore.shared.PageResponse;
import br.com.jogatinastore.shared.utils.SlugUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository repository;

    private final CategoryService categoryService;
    private final BrandService brandService;

    public ProductService(
            ProductRepository repository,
            CategoryService categoryService,
            BrandService brandService
    ) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    public PageResponse<ProductResponseDTO> getPublicProducts(
            String search, Boolean featured, Boolean isNew, UUID category, UUID brand,
            BigDecimal minPrice, BigDecimal maxPrice, Boolean onSale,
            Pageable pageable
    ) {
        logger.debug("Fetching filtered public products");

        Specification<Product> spec = Specification
            .where(ProductSpecifications.search(search))
            .and(ProductSpecifications.featured(featured))
            .and(ProductSpecifications.isNew(isNew))
            .and(ProductSpecifications.category(category))
            .and(ProductSpecifications.brand(brand))
            .and(ProductSpecifications.minPrice(minPrice))
            .and(ProductSpecifications.maxPrice(maxPrice))
            .and(ProductSpecifications.availableForSale(true))
            .and(ProductSpecifications.onSale(onSale));

        Page<Product> page = repository.findAll(spec, pageable);

        //var products = repository.findAllWithImagesByIdIn(
        //    page.getContent().stream().map(Product::getId).toList()
        //)

        var items = page.stream().map(ProductResponseDTO::new).toList();

        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    public PageResponse<ProductResponseDTO> getProducts(
            String search, Boolean active, Boolean featured, Boolean isNew, UUID category, UUID brand,
            BigDecimal minPrice, BigDecimal maxPrice, BigDecimal minCost, BigDecimal maxCost,
            Boolean availableForSale, Boolean hasInventory, Boolean onSale,
            LocalDateTime createdFrom, LocalDateTime createdTo, LocalDateTime updatedFrom, LocalDateTime updatedTo,
            Pageable pageable
    ) {
        logger.debug("Fetching filtered products");

        Specification<Product> spec = Specification
            .where(ProductSpecifications.search(search))
            .and(ProductSpecifications.active(active))
            .and(ProductSpecifications.featured(featured))
            .and(ProductSpecifications.isNew(isNew))
            .and(ProductSpecifications.category(category))
            .and(ProductSpecifications.brand(brand))
            .and(ProductSpecifications.minPrice(minPrice))
            .and(ProductSpecifications.maxPrice(maxPrice))
            .and(ProductSpecifications.minCost(minCost))
            .and(ProductSpecifications.maxCost(maxCost))
            .and(ProductSpecifications.availableForSale(availableForSale))
            .and(ProductSpecifications.hasInventory(hasInventory))
            .and(ProductSpecifications.onSale(onSale))
            .and(ProductSpecifications.createdFrom(createdFrom))
            .and(ProductSpecifications.createdTo(createdTo))
            .and(ProductSpecifications.updatedFrom(updatedFrom))
            .and(ProductSpecifications.updatedTo(updatedTo));


        Page<Product> page = repository.findAll(spec, pageable);

//        var products = repository.findAllWithImagesByIdIn(
//            page.getContent().stream().map(Product::getId).toList()
//        )

        var items = page.stream().map(ProductResponseDTO::new).toList();

        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    public ProductResponseDTO findById(UUID id) {
        logger.debug("Fetching product id={}", id);

        return new ProductResponseDTO(findEntityById(id));
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto) {
        logger.debug("Creating Product: {}", dto.title());

        try {
            String slug = SlugUtils.toSlug(dto.title());

//        var category = categoryService.getValidCategory(dto.categoryId());
//        var brand = brandService.getValidBrand(dto.brandId());

            var category = categoryService.getValidReference(dto.categoryId());
            var brand = brandService.getValidReference(dto.brandId());

            Product product = Product.createFrom(dto, slug, category, brand);

            return new ProductResponseDTO(repository.save(product));
        } catch (DataIntegrityViolationException ex) {
            handleUniqueConstraintViolation(ex);
            throw ex;
        }
    }

    @Transactional
    public ProductResponseDTO update(UUID id, ProductRequestDTO dto) {
        logger.debug("Updating Product: {}", dto.title());

        Product product = findEntityById(id);

        applyUpdate(dto, product);

        Product updated = repository.save(product);

        logger.info("Successfully updated user userId={}", updated.getTitle());

        return new ProductResponseDTO(updated);
    }

    public void deactivate(UUID id) {
        logger.debug("Deactivating Product id={}", id);

        validateProduct(id);

        repository.deactivate(id);

        logger.info("Product deactivated successfully. id={}", id);
    }

    public void activate(UUID id) {
        logger.debug("Activating Product id={}", id);

        validateProduct(id);

        repository.activate(id);

        logger.info("Product activated successfully. id={}", id);
    }

    private Product findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ProductErrors.Target.PRODUCT, ProductErrors.Code.PRODUCT_NOT_FOUND));
    }

    private void handleUniqueConstraintViolation(
            DataIntegrityViolationException ex) {

        String message = ExceptionUtils.getRootCauseMessage(ex);

        if (message.contains("uk_product_title")) {
            throw new ConflictException(
                    ProductErrors.Target.TITLE,
                    ProductErrors.Code.PRODUCT_TITLE_ALREADY_EXISTS
            );
        }

        if (message.contains("uk_product_slug")) {
            throw new ConflictException(
                    ProductErrors.Target.SLUG,
                    ProductErrors.Code.PRODUCT_SLUG_ALREADY_EXISTS
            );
        }

        if (message.contains("uk_product_barcode")) {
            throw new ConflictException(
                    ProductErrors.Target.BARCODE,
                    ProductErrors.Code.PRODUCT_BARCODE_ALREADY_EXISTS
            );
        }

        if (message.contains("uk_product_sku")) {
            throw new ConflictException(
                    ProductErrors.Target.SKU,
                    ProductErrors.Code.PRODUCT_SKU_ALREADY_EXISTS
            );
        }
    }

    private void applyUpdate(ProductRequestDTO dto, Product product) {
        product.setTitle(dto.title());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setSalePrice(dto.salePrice());
        product.setCostPrice(dto.costPrice());
        product.setSku(dto.sku());

        if (dto.categoryId() != null)
            product.setCategory(categoryService.getValidReference(dto.categoryId()));

        if (dto.brandId() != null)
            product.setBrand(brandService.getValidReference(dto.brandId()));
    }

    private void validateProduct(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    ProductErrors.Target.ID,
                    ProductErrors.Code.PRODUCT_NOT_FOUND
            );
        }
    }

    public Product getValidReference(UUID id) {
        logger.debug("Fetching Category Reference id={}", id);

        validateProduct(id);

        return repository.getReferenceById(id);
    }
}
