CREATE TABLE IF NOT EXISTS carts (
    id CHAR(36) PRIMARY KEY,

    user_id CHAR(36) NOT NULL,

    subtotal_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    -- shipping_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    -- discount_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,

    status ENUM(
       'OPEN',
       'CHECKOUT',
       'COMPLETED',
       'CANCELLED'
    ) NOT NULL DEFAULT 'OPEN',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_subtotal_amount_positive CHECK (subtotal_amount >= 0),
    CONSTRAINT chk_total_amount_positive CHECK (total_amount >= 0),

    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB;

--
CREATE TABLE IF NOT EXISTS cart_item (
    cart_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,

    unit_price DECIMAL(10, 2) NOT NULL DEFAULT 0,
    quantity INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_unit_price_positive CHECK (unit_price > 0),

    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES carts (id),
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT uk_cart_product UNIQUE (cart_id, product_id)
) ENGINE=InnoDB;
