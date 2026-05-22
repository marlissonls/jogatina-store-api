package br.com.jogatinastore.domain.authentication.service;

import br.com.jogatinastore.domain.user.entity.User;
import br.com.jogatinastore.domain.authentication.dto.AccountCredentialsDTO;
import br.com.jogatinastore.domain.authentication.dto.RefreshTokenDTO;
import br.com.jogatinastore.domain.authentication.dto.TokenDTO;
import br.com.jogatinastore.infra.security.authentication.AuthenticationFacade;
import br.com.jogatinastore.infra.security.jwt.JwtTokenProvider;
import br.com.jogatinastore.infra.security.principal.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationFacade authenticationFacade;
    private final JwtTokenProvider tokenProvider;

    public AuthService(
            AuthenticationFacade authenticationFacade,
            JwtTokenProvider tokenProvider
    ) {
        this.authenticationFacade = authenticationFacade;
        this.tokenProvider = tokenProvider;
    }

    public TokenDTO signIn(AccountCredentialsDTO credentials) {
        logger.debug("Attempting to authenticate user: '{}'", credentials.email());

        AuthenticatedUser user =
                authenticationFacade.authenticate(credentials);

        logger.info("User '{}' successfully authenticated. Generating access tokens.", user.email());

        return tokenProvider.issueTokens(
            user.id(),
            user.email(),
            user.roles()
        );
    }

    public TokenDTO refreshToken(RefreshTokenDTO refresh) {
        logger.debug("Processing token refresh request.");

        AuthenticatedUser user =
                tokenProvider.validateRefreshToken(refresh.refreshToken());

        TokenDTO tokenDTO = tokenProvider.issueTokens(
            user.id(),
            user.email(),
            user.roles()
        );

        logger.info("Access token refreshed successfully.");

        return tokenDTO;
    }
}



