package br.com.jogatinastore.iam.role.domain.exception;

public final class RoleErrors {

    private RoleErrors() {}

    // Targets
    public static final class Target {
        public static final String ROLE = "role";
        public static final String ROLE_ID = "role.id";
        public static final String ROLE_TITLE = "role.title";
        public static final String ROLE_DESCRIPTION = "role.description";
        public static final String PERMISSION = "role.permission";
    }

    // Codes
    public static final class Code {
        public static final String PERMISSION_DENIED = "error.role.permission.denied";
        public static final String ROLE_NOT_FOUND = "error.role.not_found";
        public static final String ROLE_ALREADY_EXISTS = "error.role.not_found";
    }
}