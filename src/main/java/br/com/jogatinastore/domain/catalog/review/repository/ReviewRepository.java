package br.com.jogatinastore.domain.catalog.review.repository;

import br.com.jogatinastore.domain.catalog.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
