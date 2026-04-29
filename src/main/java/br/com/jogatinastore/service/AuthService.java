package br.com.jogatinastore.service;

import br.com.jogatinastore.model.user.User;
import br.com.jogatinastore.security.dto.AccountCredentialsDTO;
import br.com.jogatinastore.security.dto.TokenDTO;
import br.com.jogatinastore.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public TokenDTO signIn(AccountCredentialsDTO credentials) {
        logger.debug("Attempting to authenticate user: '{}'", credentials.email());

        Authentication authenticated = authenticateUser(credentials);
        User user = (User) authenticated.getPrincipal();

        logger.info("User '{}' successfully authenticated. Generating access tokens.", user.getEmail());
        return tokenProvider.createAccessToken(user.getId().toString(), user.getEmail(), user.getRoles());
}

    private Authentication authenticateUser(AccountCredentialsDTO credentials) {

        logger.debug("Dispatching authentication request to AuthenticationManager for: '{}'", credentials.email());

        return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password())
        );
    }

    public TokenDTO refreshToken() {
        logger.debug("Processing token refresh request.");

        TokenDTO tokenDTO = tokenProvider.refreshToken();

        logger.info("Token refresh successfully completed.");
        return tokenDTO;
    }
}



