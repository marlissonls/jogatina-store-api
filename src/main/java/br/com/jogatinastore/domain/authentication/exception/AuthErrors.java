package br.com.jogatinastore.domain.authentication.exception;

public final class AuthErrors {

    private AuthErrors() {}

    // Targets
    public static final class Target {
        public static final String CREDENTIALS = "auth.signin.credentials";
        public static final String TOKEN = "auth.token";
        public static final String ACCESS_TOKEN = "auth.access_token";
        public static final String REFRESH_TOKEN = "auth.refresh_token";
        public static final String EMAIL = "auth.email";
        public static final String PASSWORD = "auth.password";
    }

    // Codes
    public static final class Code {
        public static final String CREDENTIALS_INVALID = "error.auth.signin.credentials.invalid";

        public static final String TOKEN_INVALID = "error.auth.token.invalid";
        public static final String ACCESS_TOKEN_INVALID = "error.auth.access_token.invalid";
        public static final String REFRESH_TOKEN_INVALID = "error.auth.refresh_token.invalid";

        public static final String AUTH_EMAIL_REQUIRED = "error.auth.email.required";
        public static final String AUTH_EMAIL_INVALID = "error.auth.email.invalid";

        public static final String AUTH_PASSWORD_REQUIRED = "error.auth.password.required";
        public static final String AUTH_PASSWORD_SIZE = "error.auth.password.size";
    }
}