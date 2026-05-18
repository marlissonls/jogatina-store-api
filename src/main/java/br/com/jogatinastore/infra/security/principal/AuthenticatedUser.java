package br.com.jogatinastore.infra.security.principal;

import java.util.List;

public record AuthenticatedUser(
        String id,
        String email,
        List<String> roles
) {
    public AuthenticatedUser {
        roles = roles == null ? List.of() : List.copyOf(roles);
    }
}