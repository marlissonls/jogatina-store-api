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

    active BOOLEAN DEFAULT FALSE,
    featured BOOLEAN DEFAULT FALSE,

    rating DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    rating_count INT NOT NULL DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_product_title UNIQUE(title),
    CONSTRAINT uk_product_slug UNIQUE(slug),
    CONSTRAINT uk_product_barcode UNIQUE(barcode),
    CONSTRAINT uk_product_sku UNIQUE(sku),

    CONSTRAINT chk_products_price CHECk (price >= 0.00),
    CONSTRAINT chk_products_sale_price CHECk (sale_price >= 0.00),
    CONSTRAINT chk_products_cost_price CHECk (cost_price >= 0.00),
    CONSTRAINT chk_products_rating CHECK (rating >= 0.00 AND rating <= 5.00),
    CONSTRAINT chk_products_rating_count CHECK (rating_count >= 0),

    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_product_brand FOREIGN KEY (brand_id) REFERENCES brands (id)
) ENGINE=InnoDB;
