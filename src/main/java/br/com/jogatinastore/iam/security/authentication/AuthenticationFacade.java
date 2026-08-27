package br.com.jogatinastore.iam.security.authentication;

import br.com.jogatinastore.iam.authentication.application.dto.AccountCredentialsDTO;
import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;

public interface AuthenticationFacade {

    AuthenticatedUser authenticate(
            AccountCredentialsDTO credentials
    );
}
