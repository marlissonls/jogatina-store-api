package br.com.jogatinastore.domain.authentication.controller;

import br.com.jogatinastore.domain.authentication.docs.AuthControllerDocs;
import br.com.jogatinastore.domain.authentication.dto.AccountCredentialsDTO;
import br.com.jogatinastore.domain.authentication.dto.RefreshTokenDTO;
import br.com.jogatinastore.domain.authentication.dto.TokenDTO;
import br.com.jogatinastore.domain.authentication.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for User Authentication")
public class AuthController implements AuthControllerDocs {

    private final AuthService service;
    private final String JSON = MediaType.APPLICATION_JSON_VALUE;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping(path = "/signin", produces = JSON)
    @Override
    public ResponseEntity<TokenDTO> signIn(@RequestBody @Valid AccountCredentialsDTO credentials) {

        return ResponseEntity.ok().body(service.signIn(credentials));
    }

    @PostMapping(path = "/refresh", produces = JSON)
    @Override
    public ResponseEntity<TokenDTO> refresh(@RequestBody @Valid RefreshTokenDTO refresh) {

        return ResponseEntity.ok().body(service.refreshToken(refresh));
    }
}
