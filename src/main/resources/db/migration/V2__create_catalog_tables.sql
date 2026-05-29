CREATE TABLE IF NOT EXISTS categories (
    id CHAR(36) PRIMARY KEY,

    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

--
CREATE TABLE IF NOT EXISTS products (
    id CHAR(36) PRIMARY KEY,

    category_id CHAR(36) NOT NULL,

    description VARCHAR(255),
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    price DECIMAL(10,2) NOT NULL,
    sale_price DECIMAL(10,2) NOT NULL,
    cost_price DECIMAL(10,2) NOT NULL,
    barcode VARCHAR(50) NOT NULL UNIQUE,
    sku VARCHAR(100) NOT NULL UNIQUE,
    tags JSON,
    rating DECIMAL(3,2) NOT NULL DEFAULT 0.00,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,

    CONSTRAINT fk_product_category
    FOREIGN KEY (category_id) REFERENCES categories (id)
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
    deleted_at TIMESTAMP DEFAULT NULL,

    CONSTRAINT fk_product_images_product
    FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE=InnoDB;

--
CREATE TABLE IF NOT EXISTS inventories (
    product_id CHAR(36) PRIMARY KEY,

    quantity SMALLINT NOT NULL DEFAULT 0,
    reserved_quantity SMALLINT NOT NULL DEFAULT 0,
    min_quantity SMALLINT NOT NULL DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,

    CONSTRAINT fk_inventories_product
    FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE=InnoDB;

--
CREATE TABLE IF NOT EXISTS favorites (
    user_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,

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

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,

    PRIMARY KEY(user_id, product_id),

    INDEX idx_reviews_product_id (product_id),

    CONSTRAINT fk_reviews_user
    FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT fk_reviews_product
    FOREIGN KEY (product_id) REFERENCES products (id),

    CONSTRAINT chk_reviews_rating
    CHECK (rating >= 0.00 AND rating <= 5.00)
) ENGINE=InnoDB;