package br.com.jogatinastore.iam.authentication.presentation.http;

import br.com.jogatinastore.iam.authentication.presentation.docs.AuthControllerDocs;
import br.com.jogatinastore.iam.authentication.application.dto.AccountCredentialsDto;
import br.com.jogatinastore.iam.authentication.application.dto.RefreshTokenDto;
import br.com.jogatinastore.iam.authentication.application.dto.TokenDto;
import br.com.jogatinastore.iam.authentication.application.service.AuthService;
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

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Override
    @PostMapping(path = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDto> signIn(@RequestBody @Valid AccountCredentialsDto credentials) {

        return ResponseEntity.ok().body(service.signIn(credentials));
    }

    @Override
    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDto> refresh(@RequestBody @Valid RefreshTokenDto refresh) {

        return ResponseEntity.ok().body(service.refreshToken(refresh));
    }
}
