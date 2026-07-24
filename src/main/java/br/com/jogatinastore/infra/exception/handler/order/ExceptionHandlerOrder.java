package br.com.jogatinastore.infra.exception.handler.order;

public final class ExceptionHandlerOrder {

    private ExceptionHandlerOrder() {}

    public static final int AUTHENTICATION = 1;
    public static final int AUTHORIZATION = 2;
    public static final int REQUEST = 3;
    public static final int RESOURCE = 4;
    public static final int CONFLICT = 5;
    public static final int INFRASTRUCTURE = 6;
}