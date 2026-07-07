package br.com.jogatinastore.domain.catalog.brand.exception;

public final class BrandErrors {

    private BrandErrors() {}

    public static final class Target {
        public static final String BRAND = "brand";
        public static final String ID = "brand.id";
        public static final String TITLE = "brand.title";
        public static final String SLUG = "brand.slug";
        public static final String DESCRIPTION = "brand.description";
        public static final String ACTIVE = "brand.active";
    }

    public static final class Code {
        public static final String BRAND_NOT_FOUND = "error.brand.not_found";

        public static final String BRAND_ID_REQUIRED = "error.brand.id.required";

        public static final String BRAND_TITLE_REQUIRED = "error.brand.title.required";
        public static final String BRAND_TITLE_SIZE = "error.brand.title.size";
        public static final String BRAND_TITLE_INVALID = "error.brand.title.invalid";
        public static final String BRAND_TITLE_ALREADY_EXISTS = "error.brand.title.already_exists";

        public static final String BRAND_SLUG_REQUIRED = "error.brand.slug.required";
        public static final String BRAND_SLUG_INVALID = "error.brand.slug.invalid";
        public static final String BRAND_SLUG_ALREADY_EXISTS = "error.brand.slug.already_exists";

        public static final String BRAND_DESCRIPTION_REQUIRED = "error.brand.description.required";
        public static final String BRAND_DESCRIPTION_SIZE = "error.brand.description.size";
        public static final String BRAND_DESCRIPTION_INVALID = "error.brand.description.invalid";

        public static final String BRAND_ACTIVE_REQUIRED = "error.brand.active.required";
        public static final String BRAND_ACTIVE_INVALID = "error.brand.active.invalid";
    }
}
