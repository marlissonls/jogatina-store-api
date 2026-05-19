package br.com.jogatinastore.domain.user.exception;

public final class UserErrors {

    private UserErrors() {}

    // Targets
    public static final class Target {
        public static final String USER = "user";
        public static final String ID = "user.id";
        public static final String NAME = "user.name";
        public static final String CPF = "user.cpf";
        public static final String BIRTHDATE = "user.birthdate";
        public static final String PHONE = "user.phone";
        public static final String EMAIL = "user.email";
        public static final String PASSWORD = "user.password";
    }

    // Codes
    public static final class Code {
        // ID
        public static final String USER_ID_REQUIRED = "error.user.id.required";

        // Name
        public static final String USER_NAME_REQUIRED = "error.user.name.required";
        public static final String USER_NAME_SIZE = "error.user.name.size";
        public static final String USER_NAME_INVALID_FORMAT = "error.user.name.invalid_format";

        // CPF
        public static final String USER_CPF_REQUIRED = "error.user.cpf.required";
        public static final String USER_CPF_INVALID = "error.user.cpf.invalid";
        public static final String USER_CPF_ALREADY_EXISTS = "error.user.cpf.already_exists";

        // Birthdate
        public static final String USER_BIRTHDATE_REQUIRED = "error.user.birthdate.required";
        public static final String USER_BIRTHDATE_PAST_REQUIRED = "error.user.birthdate.past_required";

        // Phone
        public static final String USER_PHONE_REQUIRED = "error.user.phone.required";
        public static final String USER_PHONE_INVALID = "error.user.phone.invalid";

        // Email
        public static final String USER_EMAIL_REQUIRED = "error.user.email.required";
        public static final String USER_EMAIL_INVALID = "error.user.email.invalid";
        public static final String USER_EMAIL_ALREADY_EXISTS = "error.user.email.already_exists";

        // Password
        public static final String USER_PASSWORD_REQUIRED = "error.user.password.required";
        public static final String USER_PASSWORD_SIZE = "error.user.password.size";

        // General
        public static final String USER_NOT_FOUND = "error.user.not_found";
    }
}
