package br.com.jogatinastore.exception.messages;

public interface AuthErrorCode {
    String CREDENTIALS_INVALID = "error.auth.signin.credentials.invalid";
    String TOKEN_INVALID = "error.auth.token.invalid";
    String ACCESS_TOKEN_INVALID = "error.auth.access_token.invalid";
    String REFRESH_TOKEN_INVALID = "error.auth.refresh_token.invalid";
}