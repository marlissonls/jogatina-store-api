package br.com.jogatinastore.domain.catalog.inventory.exception;

public final class InventoryErrors {

    private InventoryErrors() {}

    public final static class Target {
        public static final String INVENTORY = "inventory";
        public final static String ID = "inventory.id";
        public final static String PRODUCT = "inventory.product";
        public final static String QUANTITY = "inventory.available_quantity";
        public final static String RESERVED_QUANTITY = "inventory.reserved_quantity";
        public final static String MIN_QUANTITY = "inventory.min_quantity";
    }

    public final static class Code {
        public static final String INVENTORY_NOT_FOUND = "error.inventory.not_found";
        public static final String INVENTORY_ID_REQUIRED = "error.inventory.id.required";

        public static final String INVENTORY_PRODUCT_REQUIRED = "error.inventory.product.required";
        public static final String INVENTORY_PRODUCT_INVALID = "error.inventory.product.invalid";
        public static final String INVENTORY_PRODUCT_ALREADY_EXISTS = "error.inventory.product.already_exists";

        public static final String INVENTORY_AVAILABLE_QUANTITY_REQUIRED = "error.inventory.available_quantity.required";
        public static final String INVENTORY_AVAILABLE_QUANTITY_INVALID = "error.inventory.available_quantity.invalid";
        public static final String INVENTORY_QUANTITY_INSUFFICIENT = "error.inventory.available_quantity.insufficient";

        public static final String INVENTORY_RESERVED_QUANTITY_REQUIRED = "error.inventory.reserved_quantity.required";
        public static final String INVENTORY_RESERVED_QUANTITY_INVALID = "error.inventory.reserved_quantity.invalid";
        public static final String INVENTORY_RESERVED_QUANTITY_EXCEEDS_STOCK = "error.inventory.reserved_quantity.exceeds_stock";

        public static final String INVENTORY_MIN_QUANTITY_REQUIRED = "error.inventory.min_quantity.required";
        public static final String INVENTORY_MIN_QUANTITY_INVALID = "error.inventory.min_quantity.invalid";
        public static final String INVENTORY_MIN_QUANTITY_EXCEEDED = "error.inventory.min_quantity.exceeded";
    }
}
