package br.com.jogatinastore.infra.security.authentication;

import br.com.jogatinastore.domain.authentication.dto.AccountCredentialsDTO;
import br.com.jogatinastore.domain.user.entity.User;
import br.com.jogatinastore.infra.security.principal.AuthenticatedUser;
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

        User user = (User) authentication.getPrincipal();

        return new AuthenticatedUser(
            user.getId().toString(),
            user.getEmail(),
            user.getRoles()
        );
    }
}