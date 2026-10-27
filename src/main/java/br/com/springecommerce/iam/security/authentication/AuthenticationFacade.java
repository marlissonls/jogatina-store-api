package br.com.springecommerce.iam.security.authentication;

import br.com.springecommerce.iam.authentication.application.dto.AccountCredentialsDto;
import br.com.springecommerce.iam.security.principal.AuthenticatedUser;

public interface AuthenticationFacade {

    AuthenticatedUser authenticate(
            AccountCredentialsDto credentials
    );
}
