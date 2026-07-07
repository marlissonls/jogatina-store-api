package br.com.jogatinastore.domain.catalog.product.service;

import br.com.jogatinastore.domain.catalog.brand.service.BrandService;
import br.com.jogatinastore.domain.catalog.category.service.CategoryService;
import br.com.jogatinastore.domain.catalog.product.dto.ProductPublicResponseDTO;
import br.com.jogatinastore.domain.catalog.product.dto.ProductCreateDTO;
import br.com.jogatinastore.domain.catalog.product.dto.ProductResponseDTO;
import br.com.jogatinastore.domain.catalog.product.dto.ProductUpdateDTO;
import br.com.jogatinastore.domain.catalog.product.entity.Product;
import br.com.jogatinastore.domain.catalog.product.exception.ProductErrors;
import br.com.jogatinastore.domain.catalog.product.filter.ProductManagerFilter;
import br.com.jogatinastore.domain.catalog.product.filter.ProductPublicFilter;
import br.com.jogatinastore.domain.catalog.product.repository.ProductRepository;
import br.com.jogatinastore.domain.catalog.product.specification.ProductSpecificationFactory;
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

    public PageResponse<ProductPublicResponseDTO> getPublicViewProducts(
            ProductPublicFilter filter, Pageable pageable
    ) {
        logger.info("Search public products. filter={}", filter);

        Specification<Product> spec = ProductSpecificationFactory.publicView(filter);

        Page<Product> page = repository.findAll(spec, pageable);

//        List<Long> ids = page.getContent()
//                .stream()
//                .map(Product::getId)
//                .toList();
//
//        var images = imageRepository.findByProductIdIn(ids);
//        var stocks = stockRepository.findByProductIdIn(ids);

        var items = page.stream().map(ProductPublicResponseDTO::new).toList();

        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    public PageResponse<ProductResponseDTO> getManagerViewProducts(
            ProductManagerFilter filter, Pageable pageable
    ) {
        logger.debug("Fetching filtered products");

        Specification<Product> spec = ProductSpecificationFactory.managerView(filter);

        Page<Product> page = repository.findAll(spec, pageable);

//        List<Long> ids = page.getContent()
//                .stream()
//                .map(Product::getId)
//                .toList();
//
//        var images = imageRepository.findByProductIdIn(ids);
//        var stocks = stockRepository.findByProductIdIn(ids);

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

    public ProductPublicResponseDTO findBySlug(String slug) {
        logger.debug("Fetching product slug={}", slug);

        var product = repository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException(ProductErrors.Target.SLUG, ProductErrors.Code.PRODUCT_NOT_FOUND));

        return new ProductPublicResponseDTO(product);
    }

    public ProductResponseDTO findByBarcode(String barcode) {
        logger.debug("Fetching product barcode={}", barcode);

        var product = repository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException(ProductErrors.Target.BARCODE, ProductErrors.Code.PRODUCT_NOT_FOUND));

        return new ProductResponseDTO(product);
    }

    public ProductResponseDTO findBySku(String sku) {
        logger.debug("Fetching product sku={}", sku);

        var product = repository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException(ProductErrors.Target.SKU, ProductErrors.Code.PRODUCT_NOT_FOUND));

        return new ProductResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO create(ProductCreateDTO dto) {
        logger.debug("Creating Product: {}", dto.title());

        try {
            String slug = SlugUtils.toSlug(dto.title());

//        var category = categoryService.getValidCategory(dto.categoryId());
//        var brand = brandService.getValidBrand(dto.brandId());

            var category = categoryService.getValidReference(dto.categoryId());
            var brand = brandService.getValidReference(dto.brandId());

            Product product = Product.createFrom(dto, slug, category, brand);

            Product created = repository.save(product);

            logger.info("Product created successfully. product title={}", created.getTitle());

            return new ProductResponseDTO(created);
        } catch (DataIntegrityViolationException ex) {
            handleUniqueConstraintViolation(ex);
            throw ex;
        }
    }

    @Transactional
    public ProductResponseDTO update(UUID id, ProductUpdateDTO dto) {
        logger.debug("Updating Product: {}", dto.title());

        Product product = findEntityById(id);

        var category = dto.categoryId() != null
                ? categoryService.getValidReference(dto.categoryId())
                : null;

        var brand = dto.brandId() != null
                ? brandService.getValidReference(dto.brandId())
                : null;

        product.update(dto, category, brand);

        Product updated = repository.save(product);

        logger.info("Product updated successfully. product title={}", updated.getTitle());

        return new ProductResponseDTO(updated);
    }

    @Transactional
    public void deactivate(UUID id) {
        logger.debug("Deactivating Product id={}", id);

        validateProduct(id);

        repository.deactivate(id);

        logger.info("Product deactivated successfully. id={}", id);
    }

    @Transactional
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

    private void validateProduct(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    ProductErrors.Target.ID,
                    ProductErrors.Code.PRODUCT_NOT_FOUND
            );
        }
    }

    public Product getValidReference(UUID id) {
        logger.debug("Fetching product reference id={}", id);

        validateProduct(id);

        return repository.getReferenceById(id);
    }
}
