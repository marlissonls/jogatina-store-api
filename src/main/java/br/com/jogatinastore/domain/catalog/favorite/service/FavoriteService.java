package br.com.jogatinastore.domain.catalog.favorite.service;

import br.com.jogatinastore.domain.catalog.favorite.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository repository;

    public FavoriteService(FavoriteRepository repository) {
        this.repository = repository;
    }
}
