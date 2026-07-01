CREATE TABLE IF NOT EXISTS categories (
    id CHAR(36) PRIMARY KEY,

    title VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL UNIQUE,

    description VARCHAR(255),
    active BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

--
CREATE TABLE IF NOT EXISTS brands (
    id CHAR(36) PRIMARY KEY,

    title VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL UNIQUE,

    description VARCHAR(255),
    active BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

--
CREATE TABLE IF NOT EXISTS products (
    id CHAR(36) PRIMARY KEY,

    category_id CHAR(36) NOT NULL,
    brand_id CHAR(36) NOT NULL,

    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    sale_price DECIMAL(10,2) NULL,
    cost_price DECIMAL(10,2) NOT NULL,
    barcode VARCHAR(50) NOT NULL,
    sku VARCHAR(100) NOT NULL,
    tags JSON,
    rating DECIMAL(3,2) NOT NULL DEFAULT 0.00,

    active BOOLEAN DEFAULT TRUE,
    featured BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_product_title UNIQUE(title),
    CONSTRAINT uk_product_slug UNIQUE(slug),
    CONSTRAINT uk_product_barcode UNIQUE(barcode),
    CONSTRAINT uk_product_sku UNIQUE(sku);

    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_product_brand FOREIGN KEY (brand_id) REFERENCES brands (id);
) ENGINE=InnoDB;

--
CREATE TABLE IF NOT EXISTS product_images (
    id CHAR(36) PRIMARY KEY,

    product_id CHAR(36) NOT NULL,

    url VARCHAR(255) NOT NULL,
    filename VARCHAR(255),
    alt_text VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    sort_order SMALLINT DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_images_product
    FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE=InnoDB;

--
CREATE TABLE IF NOT EXISTS inventories (
    id CHAR(36) PRIMARY KEY,

    product_id CHAR(36) NOT NULL,
    -- warehouse_id CHAR(36) NOT NULL,
    -- location VARCHAR(255),

    available_quantity INTEGER NOT NULL DEFAULT 0,
    reserved_quantity INTEGER NOT NULL DEFAULT 0,
    min_quantity INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_inventory_available_quantity CHECK (available_quantity >= 0),
    CONSTRAINT chk_inventory_reserved_quantity CHECK (reserved_quantity >= 0),
    CONSTRAINT chk_inventory_min_quantity CHECK (min_quantity >= 0),

    CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products(id),

    CONSTRAINT uk_product UNIQUE(product_id); --will change to CONSTRAINT uk_inventory_product_warehouse UNIQUE(product_id, warehouse_id)

    -- CONSTRAINT fk_inventory_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),

    -- CONSTRAINT uk_inventory_product_warehouse UNIQUE(product_id, warehouse_id)
) ENGINE=InnoDB;

--
CREATE TABLE IF NOT EXISTS favorites (
    user_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(user_id, product_id),

    INDEX idx_favorites_product_id (product_id),

    CONSTRAINT fk_favorites_user
    FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT fk_favorites_product
    FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE=InnoDB;

--
CREATE TABLE IF NOT EXISTS reviews (
    user_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,

    rating DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    comment TEXT,

    hidden BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY(user_id, product_id),

    INDEX idx_reviews_product_id (product_id),

    CONSTRAINT fk_reviews_user
    FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT fk_reviews_product
    FOREIGN KEY (product_id) REFERENCES products (id),

    CONSTRAINT chk_reviews_rating
    CHECK (rating >= 0.00 AND rating <= 5.00)
) ENGINE=InnoDB;