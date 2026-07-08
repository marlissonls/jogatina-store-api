CREATE TABLE IF NOT EXISTS stocks (
    id CHAR(36) PRIMARY KEY,

    product_id CHAR(36) NOT NULL,
    -- warehouse_id CHAR(36) NOT NULL,
    -- location VARCHAR(255),

    available_quantity INTEGER NOT NULL DEFAULT 0,
    reserved_quantity INTEGER NOT NULL DEFAULT 0,
    minimum_quantity INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_inventory_available_quantity CHECK (available_quantity >= 0),
    CONSTRAINT chk_inventory_reserved_quantity CHECK (reserved_quantity >= 0),
    CONSTRAINT chk_inventory_minimum_quantity CHECK (minimum_quantity >= 0),

    CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products (id),

    CONSTRAINT uk_product UNIQUE (product_id) -- will change to CONSTRAINT uk_inventory_product_warehouse UNIQUE(product_id, warehouse_id)

-- CONSTRAINT fk_inventory_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),

-- CONSTRAINT uk_inventory_product_warehouse UNIQUE(product_id, warehouse_id)
    ) ENGINE=InnoDB;