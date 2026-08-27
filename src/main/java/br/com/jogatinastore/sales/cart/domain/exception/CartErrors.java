package br.com.jogatinastore.sales.cart.domain.exception;

public final class CartErrors {

    private CartErrors() {}

    public static final class Target {

        public static final String CART = "cart";
        public static final String ID = "cart.id";
        public static final String CUSTOMER_ID = "cart.customer.id";
        public static final String PRODUCT_ID = "cart.product.id";
        public static final String SUB_TOTAL_AMOUNT = "cart.subtotal_amount";
        public static final String STATUS = "cart.status";
        public static final String ITEM = "cart.item";
    }

    public static final class Code {

        public static final String CART_NOT_FOUND = "error.cart.not_found";
        public static final String CART_ID_REQUIRED = "error.cart.id.required";
        public static final String CART_CUSTOMER_ID_REQUIRED = "error.cart.customer.id.required";
        public static final String CART_ITEM_PRODUCT_ID_REQUIRED = "error.cart.item.product.id.required";
        public static final String CART_ITEM_NOT_FOUND = "error.cart.item.not_found";
        public static final String CART_ITEM_UNAVAILABLE = "error.cart.item.unavailable";
        public static final String CART_ITEM_QUANTITY_INVALID = "error.cart.item.quantity.invalid";
        public static final String CART_ITEM_UNIT_PRICE_INVALID = "error.cart.item.unit_price.invalid";
    }
}
