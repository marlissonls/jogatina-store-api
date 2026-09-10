package br.com.jogatinastore.integrationtests.fixture;

import br.com.jogatinastore.catalog.brand.domain.model.Brand;
import br.com.jogatinastore.catalog.brand.infrastructure.persistence.BrandRepository;
import org.springframework.stereotype.Component;

@Component
public class BrandFixture {

    private final BrandRepository repository;

    private String title = "Nike";
    private String slug = "nike";
    private String description = "Descrição da marca";

    public BrandFixture(BrandRepository repository) {
        this.repository = repository;
    }

    public BrandFixture withTitle(String title) {
        this.title = title;
        return this;
    }

    public BrandFixture withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public BrandFixture withDescription(String description) {
        this.description = description;
        return this;
    }

    public Brand create() {
        Brand brand = new Brand(
                title,
                slug,
                description
        );

        return repository.save(brand);
    }
}
