package br.com.jogatinastore.domain.catalog.product.exception;

public final class ProductErrors {

    private ProductErrors() {}

    public final static class Target {
        public static final String PRODUCT = "product";
        public static final String ID = "product.id";
        public static final String TITLE = "product.title";
        public static final String SLUG = "product.slug";
        public static final String PRICE = "product.price";
        public static final String DESCRIPTION = "product.description";
        public static final String BARCODE = "product.barcode";
        public static final String SKU = "product.sku";
        public static final String CATEGORY = "product.category";
        public static final String BRAND = "product.brand";
    }

    public final static class Code {
        public static final String PRODUCT_NOT_FOUND = "error.product.not_found";

        public static final String PRODUCT_ID_REQUIRED = "error.product.id.required";

        public static final String PRODUCT_TITLE_REQUIRED = "error.product.title.required";
        public static final String PRODUCT_TITLE_SIZE = "error.product.title.size";
        public static final String PRODUCT_TITLE_INVALID = "error.product.title.invalid";
        public static final String PRODUCT_TITLE_ALREADY_EXISTS = "error.product.title.already_exists";

        public static final String PRODUCT_SLUG_REQUIRED = "error.product.slug.required";
        public static final String PRODUCT_SLUG_INVALID = "error.product.slug.invalid";
        public static final String PRODUCT_SLUG_ALREADY_EXISTS = "error.product.slug.already_exists";

        public static final String PRODUCT_DESCRIPTION_REQUIRED = "error.product.description.required";
        public static final String PRODUCT_DESCRIPTION_SIZE = "error.product.description.size";
        public static final String PRODUCT_DESCRIPTION_INVALID = "error.product.description.invalid";

        public static final String PRODUCT_PRICE_REQUIRED = "error.product.price.required";
        public static final String PRODUCT_PRICE_INVALID = "error.product.price.invalid";

        public static final String PRODUCT_SALE_PRICE_REQUIRED = "error.product.sale_price.required";
        public static final String PRODUCT_SALE_PRICE_INVALID = "error.product.sale_price.invalid";

        public static final String PRODUCT_COST_PRICE_REQUIRED = "error.product.cost_price.required";
        public static final String PRODUCT_COST_PRICE_INVALID = "error.product.cost_price.invalid";

        public static final String PRODUCT_BARCODE_REQUIRED = "error.product.barcode.required";
        public static final String PRODUCT_BARCODE_SIZE = "error.product.barcode.size";
        public static final String PRODUCT_BARCODE_INVALID = "error.product.barcode.invalid";
        public static final String PRODUCT_BARCODE_ALREADY_EXISTS = "error.product.barcode.already_exists";

        public static final String PRODUCT_SKU_REQUIRED = "error.product.sku.required";
        public static final String PRODUCT_SKU_SIZE = "error.product.sku.size";
        public static final String PRODUCT_SKU_INVALID = "error.product.sku.invalid";
        public static final String PRODUCT_SKU_ALREADY_EXISTS = "error.product.sku.already_exists";

        public static final String PRODUCT_CATEGORY_REQUIRED = "error.product.category.required";

        public static final String PRODUCT_BRAND_REQUIRED = "error.product.brand.required";
    }
}
