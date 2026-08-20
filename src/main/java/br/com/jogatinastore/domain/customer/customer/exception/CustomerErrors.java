package br.com.jogatinastore.domain.customer.customer.exception;

public final class CustomerErrors {

    private CustomerErrors() {}

    // Targets
    public static final class Target {
        public static final String CUSTOMER = "customer";
        public static final String ID = "customer.id";
        public static final String NAME = "customer.name";
        public static final String CPF = "customer.cpf";
        public static final String BIRTHDATE = "customer.birthdate";
        public static final String PHONE = "customer.phone";
    }

    // Codes
    public static final class Code {
        // ID
        public static final String CUSTOMER_ID_REQUIRED = "error.customer.id.required";

        // Name
        public static final String CUSTOMER_NAME_REQUIRED = "error.customer.name.required";
        public static final String CUSTOMER_NAME_SIZE = "error.customer.name.size";
        public static final String CUSTOMER_NAME_INVALID_FORMAT = "error.customer.name.invalid_format";

        // CPF
        public static final String CUSTOMER_CPF_REQUIRED = "error.customer.cpf.required";
        public static final String CUSTOMER_CPF_INVALID = "error.customer.cpf.invalid";
        public static final String CUSTOMER_CPF_ALREADY_EXISTS = "error.customer.cpf.already_exists";

        // Birthdate
        public static final String CUSTOMER_BIRTHDATE_REQUIRED = "error.customer.birthdate.required";
        public static final String CUSTOMER_BIRTHDATE_PAST_REQUIRED = "error.customer.birthdate.past_required";

        // Phone
        public static final String CUSTOMER_PHONE_REQUIRED = "error.customer.phone.required";
        public static final String CUSTOMER_PHONE_INVALID = "error.customer.phone.invalid";

        // General
        public static final String CUSTOMER_NOT_FOUND = "error.customer.not_found";
        public static final String CUSTOMER_ALREADY_EXISTS = "error.customer.already_exists";
    }
}
