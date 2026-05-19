package br.com.jogatinastore.domain.authorization.exception;

public final class AuthorizationErrors {

    private AuthorizationErrors() {}

    // Targets
    public static final class Target {
        public static final String AUTHZ = "authz";
        public static final String PERMISSION = "authz.permission";
        public static final String PERMISSION_ID = "authz.permission.id";
        public static final String PERMISSION_TITLE = "authz.permission.title";
        public static final String PERMISSION_DESCRIPTION = "authz.permission.description";
    }

    // Codes
    public static final class Code {
        public static final String ACCESS_DENIED = "error.authz.access.denied";
        public static final String PERMISSION_DENIED = "error.authz.permission.denied";
        public static final String PERMISSION_NOT_FOUND = "error.authz.permission.not_found";
    }
}