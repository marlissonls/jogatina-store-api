package br.com.jogatinastore.iam.authentication.application.service;

import br.com.jogatinastore.iam.authentication.application.dto.AccountCredentialsDto;
import br.com.jogatinastore.iam.authentication.application.dto.RefreshTokenDto;
import br.com.jogatinastore.iam.authentication.application.dto.TokenDto;
import br.com.jogatinastore.iam.security.authentication.AuthenticationFacade;
import br.com.jogatinastore.config.security.jwt.JwtTokenProvider;
import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public TokenDto signIn(AccountCredentialsDto credentials) {
        logger.debug("Attempting to authenticate user: '{}'", credentials.email());

        AuthenticatedUser user =
                authenticationFacade.authenticate(credentials);

        TokenDto token = tokenProvider.issueTokens(
                user.getId(),
                user.getEmail(),
                user.getAuthorities()
        );

        logger.info("User '{}' successfully authenticated.", user.getEmail());

        return token;
    }

    public TokenDto refreshToken(RefreshTokenDto refresh) {
        logger.debug("Processing token refresh request.");

        AuthenticatedUser user =
                tokenProvider.validateRefreshToken(refresh.refreshToken());

        TokenDto token = tokenProvider.issueTokens(
            user.getId(),
            user.getEmail(),
            user.getAuthorities()
        );

        logger.info("Access token refreshed successfully.");

        return token;
    }
}



