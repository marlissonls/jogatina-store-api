package br.com.jogatinastore.config.security.authentication;

import br.com.jogatinastore.iam.authentication.application.dto.AccountCredentialsDTO;
import br.com.jogatinastore.iam.security.authentication.AuthenticationFacade;
import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SpringAuthenticationFacade implements AuthenticationFacade {

    private final AuthenticationManager authenticationManager;

    public SpringAuthenticationFacade(
            AuthenticationManager authenticationManager
    ) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticatedUser authenticate(AccountCredentialsDTO credentials) {

        Authentication authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    credentials.email(),
                    credentials.password()
                )
            );

        return (AuthenticatedUser) authentication.getPrincipal();
    }
}