package br.com.jogatinastore.domain.catalog.brand.service;

import br.com.jogatinastore.domain.catalog.brand.repository.BrandRepository;
import br.com.jogatinastore.domain.catalog.brand.dto.BrandPublicDTO;
import br.com.jogatinastore.domain.catalog.brand.dto.BrandRequestDTO;
import br.com.jogatinastore.domain.catalog.brand.dto.BrandResponseDTO;
import br.com.jogatinastore.domain.catalog.brand.entity.Brand;
import br.com.jogatinastore.domain.catalog.brand.exception.BrandErrors;
import br.com.jogatinastore.infra.exception.ConflictException;
import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
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

    public List<BrandPublicDTO> findPublicCategories() {
        logger.debug("Fetching public Categories");

        return repository.findByActiveTrue()
            .stream()
            .map(BrandPublicDTO::new)
            .toList();
    }

    public BrandPublicDTO findBySlug(String slug) {
        logger.debug("Fetching brand slug={}", slug);

        var brand = repository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException(
                        BrandErrors.Target.SLUG,
                        BrandErrors.Code.BRAND_NOT_FOUND
                ));

        return new BrandPublicDTO(brand);
    }

    public List<BrandResponseDTO> findAll() {
        logger.debug("Fetching all Categories");

        return repository.findAll()
            .stream()
            .map(BrandResponseDTO::new)
            .toList();
    }

    @Transactional
    public BrandResponseDTO create(BrandRequestDTO dto) {
        logger.debug("Creating brand: {}", dto.title());

        String slug = SlugUtils.toSlug(dto.title());

        validateUniqueness(dto.title(), slug);

        Brand brand = Brand.createFrom(dto.title(), slug, dto.description());

        var saved = repository.save(brand);

        logger.info("Brand created successfully. id={}", saved.getId());

        return new BrandResponseDTO(saved);
    }

    public BrandResponseDTO findById(UUID id) {
        logger.debug("Fetching brand id={}", id);
        return new BrandResponseDTO(findEntityById(id));
    }

    public Brand getValidReference(UUID id) {
        logger.debug("Fetching brand Reference id={}", id);

        validateBrand(id);

        return repository.getReferenceById(id);
    }

    @Transactional
    public BrandResponseDTO update(UUID id, BrandRequestDTO dto) {
        logger.debug("Updating brand id={}", id);

        Brand brand = findEntityById(id);

        brand.setTitle(dto.title());
        brand.setDescription((dto.description()));

        repository.save(brand);

        logger.info("Brand updated successfully id={}", id);

        return new BrandResponseDTO(brand);
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
