package br.com.jogatinastore.domain.catalog.review.controller;

import br.com.jogatinastore.domain.catalog.review.service.ReviewService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews/v1")
public class ReviewController {

    private final ReviewService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public ReviewController(ReviewService service) {
        this.service = service;
    }
}
