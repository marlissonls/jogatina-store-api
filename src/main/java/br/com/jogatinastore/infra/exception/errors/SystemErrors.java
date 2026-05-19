package br.com.jogatinastore.infra.exception.errors;

public final class SystemErrors {

    private SystemErrors() {}

    public static final class Target {
        public static final String SYSTEM = "system";
    }

    public static final class Code {
        public static final String INTERNAL =
                "error.internal";
    }
}