package br.com.jogatinastore.domain.authorization.exception;

public final class AuthorizationErrors {

    private AuthorizationErrors() {}

    // Targets
    public static final class Target {
        public static final String AUTHZ = "authz";
        public static final String ROLE = "authz.role";
        public static final String ROLE_ID = "authz.role.id";
        public static final String ROLE_TITLE = "authz.role.title";
        public static final String ROLE_DESCRIPTION = "authz.role.description";
    }

    // Codes
    public static final class Code {
        public static final String ACCESS_DENIED = "error.authz.access.denied";
        public static final String ROLE_NOT_FOUND = "error.authz.role.not_found";
    }
}