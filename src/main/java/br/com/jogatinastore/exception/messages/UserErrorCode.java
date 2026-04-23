package br.com.jogatinastore.exception.messages;

public interface UserErrorCode {

    // ID
    String USER_ID_REQUIRED = "error.user.id.required";

    // Name
    String USER_NAME_REQUIRED = "error.user.name.required";
    String USER_NAME_SIZE = "error.user.name.size";
    String USER_NAME_INVALID_FORMAT = "error.user.name.invalid_format";

    // CPF
    String USER_CPF_REQUIRED = "error.user.cpf.required";
    String USER_CPF_INVALID = "error.user.cpf.invalid";
    String USER_CPF_ALREADY_EXISTS = "error.user.cpf.already_exists";

    // Birthdate
    String USER_BIRTHDATE_REQUIRED = "error.user.birthdate.required";
    String USER_BIRTHDATE_PAST_REQUIRED = "error.user.birthdate.past_required";

    // Phone
    String USER_PHONE_REQUIRED = "error.user.phone.required";
    String USER_PHONE_INVALID = "error.user.phone.invalid";

    // Email
    String USER_EMAIL_REQUIRED = "error.user.email.required";
    String USER_EMAIL_INVALID = "error.user.email.invalid";
    String USER_EMAIL_ALREADY_EXISTS = "error.user.email.already_exists";

    // Pass
    String USER_PASSWORD_REQUIRED = "error.user.password.required";
    String USER_PASSWORD_SIZE = "error.user.password.size";

    // General
    String USER_NOT_FOUND = "error.user.not_found";
}
