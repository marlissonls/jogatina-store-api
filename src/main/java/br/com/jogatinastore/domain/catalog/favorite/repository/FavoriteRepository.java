package br.com.jogatinastore.domain.catalog.favorite.repository;

import br.com.jogatinastore.domain.catalog.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
}
