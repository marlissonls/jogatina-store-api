package br.com.jogatinastore.domain.catalog.review.service;

import br.com.jogatinastore.domain.catalog.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }
}
