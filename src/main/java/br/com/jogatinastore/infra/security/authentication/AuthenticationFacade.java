package br.com.jogatinastore.infra.security.authentication;

import br.com.jogatinastore.domain.authentication.dto.AccountCredentialsDTO;
import br.com.jogatinastore.infra.security.principal.AuthenticatedUser;

public interface AuthenticationFacade {

    AuthenticatedUser authenticate(
            AccountCredentialsDTO credentials
    );
}
