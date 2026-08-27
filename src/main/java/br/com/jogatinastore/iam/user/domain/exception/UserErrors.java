package br.com.jogatinastore.iam.user.domain.exception;

public final class UserErrors {

    private UserErrors() {}

    // Targets
    public static final class Target {
        public static final String USER = "user";
        public static final String ID = "user.id";
        public static final String EMAIL = "user.email";
        public static final String PASSWORD = "user.password";
        public static final String USER_ROLE = "user.role";
    }

    // Codes
    public static final class Code {
        // ID
        public static final String USER_ID_REQUIRED = "error.user.id.required";
        public static final String USER_ID_INVALID = "error.user.id.invalid";

        // Email
        public static final String USER_EMAIL_REQUIRED = "error.user.email.required";
        public static final String USER_EMAIL_INVALID = "error.user.email.invalid";
        public static final String USER_EMAIL_ALREADY_EXISTS = "error.user.email.already_exists";

        // Password
        public static final String USER_PASSWORD_REQUIRED = "error.user.password.required";
        public static final String USER_PASSWORD_SIZE = "error.user.password.size";

        // Roles
        public static final String USER_ROLE_IDS_REQUIRED = "error.user.roles.id.required";
        public static final String USER_ROLE_ID_INVALID = "error.user.role.id.invalid";
        public static final String USER_ROLE_NOT_ASSIGNED = "error.user.role.not_assigned";
        public static final String USER_ROLE_CANNOT_REMOVE_LAST = "error.user.role.cannot_remove_last";

        // General
        public static final String USER_NOT_FOUND = "error.user.not_found";
    }
}
