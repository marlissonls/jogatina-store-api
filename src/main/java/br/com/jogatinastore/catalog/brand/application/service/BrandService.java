package br.com.jogatinastore.catalog.brand.application.service;

import br.com.jogatinastore.catalog.brand.infrastructure.persistence.BrandRepository;
import br.com.jogatinastore.catalog.brand.application.dto.BrandPublicDto;
import br.com.jogatinastore.catalog.brand.application.dto.BrandRequestDto;
import br.com.jogatinastore.catalog.brand.application.dto.BrandResponseDto;
import br.com.jogatinastore.catalog.brand.domain.model.Brand;
import br.com.jogatinastore.catalog.brand.domain.exception.BrandErrors;
import br.com.jogatinastore.shared.exception.base.ConflictException;
import br.com.jogatinastore.shared.exception.base.ResourceNotFoundException;
import br.com.jogatinastore.shared.utils.SlugUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BrandService {

    private final Logger logger = LoggerFactory.getLogger(BrandService.class);
    private final BrandRepository repository;

    public BrandService(BrandRepository repository) {
        this.repository = repository;
    }

    public List<BrandPublicDto> findPublicCategories() {
        logger.debug("Fetching public Categories");

        return repository.findByActiveTrue()
            .stream()
            .map(BrandPublicDto::new)
            .toList();
    }

    public BrandPublicDto findBySlug(String slug) {
        logger.debug("Fetching brand slug={}", slug);

        var brand = repository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException(
                        BrandErrors.Target.SLUG,
                        BrandErrors.Code.BRAND_NOT_FOUND
                ));

        return new BrandPublicDto(brand);
    }

    public List<BrandResponseDto> findAll() {
        logger.debug("Fetching all Categories");

        return repository.findAll()
            .stream()
            .map(BrandResponseDto::new)
            .toList();
    }

    @Transactional
    public BrandResponseDto create(BrandRequestDto dto) {
        logger.debug("Creating brand: {}", dto.title());

        String slug = SlugUtils.toSlug(dto.title());

        validateUniqueness(dto.title(), slug);

        Brand brand = Brand.createFrom(dto.title(), slug, dto.description());

        var saved = repository.save(brand);

        logger.info("Brand created successfully. id={}", saved.getId());

        return new BrandResponseDto(saved);
    }

    public BrandResponseDto findById(UUID id) {
        logger.debug("Fetching brand id={}", id);
        return new BrandResponseDto(findEntityById(id));
    }

    public Brand getValidReference(UUID id) {
        logger.debug("Fetching brand Reference id={}", id);

        validateBrand(id);

        return repository.getReferenceById(id);
    }

    @Transactional
    public BrandResponseDto update(UUID id, BrandRequestDto dto) {
        logger.debug("Updating brand id={}", id);

        Brand brand = findEntityById(id);

        brand.setTitle(dto.title());
        brand.setDescription((dto.description()));

        repository.save(brand);

        logger.info("Brand updated successfully id={}", id);

        return new BrandResponseDto(brand);
    }

    @Transactional
    public void deactivate(UUID id) {
        logger.debug("Deactivating brand id={}", id);

        validateBrand(id);

        repository.deactivate(id);

        logger.info("Brand deactivated successfully. id={}", id);
    }

    @Transactional
    public void activate(UUID id) {
        logger.debug("Activating brand id={}", id);

        validateBrand(id);

        repository.activate(id);

        logger.info("Brand activated successfully. id={}", id);
    }

    private void validateBrand(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    BrandErrors.Target.ID,
                    BrandErrors.Code.BRAND_NOT_FOUND
            );
        }
    }

//    public Brand getValidBrand(UUID id) {
//        return findEntityById(id);
//    }

    private Brand findEntityById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Brand not found. id={}", id);
                return new ResourceNotFoundException(BrandErrors.Target.ID, BrandErrors.Code.BRAND_NOT_FOUND);
            });
    }

    private void validateUniqueness(String title, String slug) {
        if (repository.existsByTitle(title)) {
            throw new ConflictException(BrandErrors.Target.TITLE, BrandErrors.Code.BRAND_TITLE_ALREADY_EXISTS);
        }
        if (repository.existsBySlug(slug)) {
            throw new ConflictException(BrandErrors.Target.SLUG, BrandErrors.Code.BRAND_SLUG_ALREADY_EXISTS);
        }
    }
}
