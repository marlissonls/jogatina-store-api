package br.com.jogatinastore.domain.sales.order.exception;

public final class OrderErrors {

    private OrderErrors() {}

    public static final class Target {

        public static final String ORDER = "order";
        public static final String ID = "order.id";
        public static final String USER_ID = "order.user.id";
        public static final String STATUS = "order.status";
        public static final String SUBTOTAL_AMOUNT = "order.subtotal_amount";
        public static final String DISCOUNT_AMOUNT = "order.discount_amount";
        public static final String SHIPPING_AMOUNT = "order.shipping_amount";
        public static final String TOTAL_AMOUNT = "order.total_amount";
        public static final String ITEM = "order.item";
    }

    public static final class Code {

        public static final String ORDER_NOT_FOUND = "error.order.not_found";
        public static final String ORDER_ID_REQUIRED = "error.order.id.required";
        public static final String ORDER_USER_ID_REQUIRED = "error.order.user.id.required";

        public static final String ORDER_ITEM_NOT_FOUND = "error.order.item.not_found";
        public static final String ORDER_ITEM_PRODUCT_ID_REQUIRED = "error.order.item.product.id.required";
        public static final String ORDER_ITEM_QUANTITY_INVALID = "error.order.item.quantity.invalid";
        public static final String ORDER_ITEM_UNIT_PRICE_INVALID = "error.order.item.unit_price.invalid";

        public static final String ORDER_STATUS_INVALID = "error.order.status.invalid";
        public static final String ORDER_CANNOT_BE_CANCELLED = "error.order.cannot_be_cancelled";
    }
}