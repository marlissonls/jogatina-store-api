package br.com.jogatinastore.integrationtests.fixture;

import br.com.jogatinastore.catalog.category.domain.model.Category;
import br.com.jogatinastore.catalog.category.infrastructure.persistence.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class CategoryFixture {

    private final CategoryRepository repository;

    private String title = "Camisas";
    private String slug = "camisas";
    private String description = "Descrição da categoria";

    public CategoryFixture(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryFixture withTitle(String title) {
        this.title = title;
        return this;
    }

    public CategoryFixture withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public CategoryFixture withDescription(String description) {
        this.description = description;
        return this;
    }

    public Category create() {
        Category category = new Category(
                title,
                slug,
                description
        );

        return repository.save(category);
    }
}
