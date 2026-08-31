package br.com.jogatinastore.iam.security.authentication;

import br.com.jogatinastore.iam.authentication.application.dto.AccountCredentialsDto;
import br.com.jogatinastore.iam.security.principal.AuthenticatedUser;

public interface AuthenticationFacade {

    AuthenticatedUser authenticate(
            AccountCredentialsDto credentials
    );
}
