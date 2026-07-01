package br.com.jogatinastore.domain.catalog.favorite.controller;

import br.com.jogatinastore.domain.catalog.favorite.service.FavoriteService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites/v1")
public class FavoriteController {

    private final FavoriteService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public FavoriteController(FavoriteService service) {
        this.service = service;
    }
}
