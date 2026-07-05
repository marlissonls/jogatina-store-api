package br.com.jogatinastore.domain.catalog.category.service;

import br.com.jogatinastore.domain.catalog.category.dto.CategoryPublicDTO;
import br.com.jogatinastore.domain.catalog.category.dto.CategoryRequestDTO;
import br.com.jogatinastore.domain.catalog.category.dto.CategoryResponseDTO;
import br.com.jogatinastore.domain.catalog.category.entity.Category;
import br.com.jogatinastore.domain.catalog.category.exception.CategoryErrors;
import br.com.jogatinastore.domain.catalog.category.repository.CategoryRepository;
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
public class CategoryService {

    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryPublicDTO> findPublicCategories() {
        logger.debug("Fetching public Categories");

        return repository.findByActiveTrue()
            .stream()
            .map(CategoryPublicDTO::new)
            .toList();
    }

    public CategoryPublicDTO findBySlug(String slug) {
        logger.debug("Fetching category slug={}", slug);

        var category = repository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CategoryErrors.Target.SLUG,
                        CategoryErrors.Code.CATEGORY_NOT_FOUND
                ));

        return new CategoryPublicDTO(category);
    }

    public List<CategoryResponseDTO> findAll() {
        logger.debug("Fetching all Categories");

        return repository.findAll()
            .stream()
            .map(CategoryResponseDTO::new)
            .toList();
    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        logger.debug("Creating Category: {}", dto.title());

        String slug = SlugUtils.toSlug(dto.title());

        validateUniqueness(dto.title(), slug);

        Category category = Category.createFrom(dto.title(), slug, dto.description());

        var saved = repository.save(category);

        logger.info("Category created successfully. id={}", saved.getId());

        return new CategoryResponseDTO(saved);
    }

    public CategoryResponseDTO findById(UUID id) {
        logger.debug("Fetching category id={}", id);
        return new CategoryResponseDTO(findEntityById(id));
    }

    public Category getValidReference(UUID id) {
        logger.debug("Fetching Category Reference id={}", id);

        validateCategory(id);

        return repository.getReferenceById(id);
    }

    @Transactional
    public CategoryResponseDTO update(UUID id, CategoryRequestDTO dto) {
        logger.debug("Updating category id={}", id);

        Category category = findEntityById(id);

        category.setTitle(dto.title());
        category.setDescription((dto.description()));

        repository.save(category);

        logger.info("Category updated successfully id={}", id);

        return new CategoryResponseDTO(category);
    }

    @Transactional
    public void deactivate(UUID id) {
        logger.debug("Deactivating category id={}", id);

        validateCategory(id);

        repository.deactivate(id);

        logger.info("Category deactivated successfully. id={}", id);
    }

    @Transactional
    public void activate(UUID id) {
        logger.debug("Activating category id={}", id);

        validateCategory(id);

        repository.activate(id);

        logger.info("Category activated successfully. id={}", id);
    }

    private void validateCategory(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    CategoryErrors.Target.ID,
                    CategoryErrors.Code.CATEGORY_NOT_FOUND
            );
        }
    }

//    public Category getValidCategory(UUID id) {
//        return findEntityById(id);
//    }

    private Category findEntityById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Category not found. id={}", id);
                return new ResourceNotFoundException(CategoryErrors.Target.ID, CategoryErrors.Code.CATEGORY_NOT_FOUND);
            });
    }

    private void validateUniqueness(String title, String slug) {
        if (repository.existsByTitle(title)) {
            throw new ConflictException(CategoryErrors.Target.TITLE, CategoryErrors.Code.CATEGORY_TITLE_ALREADY_EXISTS);
        }
        if (repository.existsBySlug(slug)) {
            throw new ConflictException(CategoryErrors.Target.SLUG, CategoryErrors.Code.CATEGORY_SLUG_ALREADY_EXISTS);
        }
    }
}
