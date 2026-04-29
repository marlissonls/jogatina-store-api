package br.com.jogatinastore.controller;

import br.com.jogatinastore.security.dto.AccountCredentialsDTO;
import br.com.jogatinastore.security.dto.TokenDTO;
import br.com.jogatinastore.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDTO> signIn(@RequestBody @Valid AccountCredentialsDTO credentials) {

        return ResponseEntity.ok().body(service.signIn(credentials));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDTO> refresh() {

        return ResponseEntity.ok().body(service.refreshToken());
    }
}
