package br.com.jogatinastore.infra.exception.errors;

public final class RequestErrors {

    private RequestErrors() {}

    // Targets
    public static final class Target {
        public static final String REQUEST = "request";

        public static final String PAYLOAD = "request.payload";

        public static final String PATH_PARAMETER =
                "request.path_parameter";

        public static final String QUERY_PARAMETER =
                "request.query_parameter";
    }

    // Codes
    public static final class Code {
        public static final String INVALID_PAYLOAD =
                "error.request.payload.invalid";

        public static final String INVALID_PATH_PARAMETER =
                "error.request.path_parameter.invalid";

        public static final String INVALID_QUERY_PARAMETER =
                "error.request.query_parameter.invalid";

        public static final String MALFORMED_REQUEST =
                "error.request.malformed";
    }
}