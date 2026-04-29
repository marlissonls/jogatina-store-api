package br.com.jogatinastore.security.principal;

import java.util.List;

public record AuthenticatedUser(
        String id,
        String email,
        List<String> roles
) {}