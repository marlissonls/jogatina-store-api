package br.com.jogatinastore.domain.catalog.category.exception;

public final class CategoryErrors {

    private CategoryErrors() {}

    public static final class Target {
        public static final String CATEGORY = "category";
        public static final String ID = "category.id";
        public static final String TITLE = "category.title";
        public static final String SLUG = "category.slug";
        public static final String DESCRIPTION = "category.description";
        public static final String ACTIVE = "category.active";
    }

    public static final class Code {
        public static final String CATEGORY_NOT_FOUND = "error.category.not_found";

        public static final String CATEGORY_ID_REQUIRED = "error.category.id.required";

        public static final String CATEGORY_TITLE_REQUIRED = "error.category.title.required";
        public static final String CATEGORY_TITLE_SIZE = "error.category.title.size";
        public static final String CATEGORY_TITLE_INVALID = "error.category.title.invalid";
        public static final String CATEGORY_TITLE_ALREADY_EXISTS = "error.category.title.already_exists";

        public static final String CATEGORY_SLUG_REQUIRED = "error.category.slug.required";
        public static final String CATEGORY_SLUG_INVALID = "error.category.slug.invalid";
        public static final String CATEGORY_SLUG_ALREADY_EXISTS = "error.category.slug.already_exists";

        public static final String CATEGORY_DESCRIPTION_REQUIRED = "error.category.description.required";
        public static final String CATEGORY_DESCRIPTION_SIZE = "error.category.description.size";
        public static final String CATEGORY_DESCRIPTION_INVALID = "error.category.description.invalid";

        public static final String CATEGORY_BARCODE_ALREADY_EXISTS = "error.category.barcode.already_exists";


        public static final String CATEGORY_ACTIVE_REQUIRED = "error.category.active.required";
        public static final String CATEGORY_ACTIVE_INVALID = "error.category.active.invalid";
    }
}
