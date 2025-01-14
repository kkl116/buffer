CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(500) NOT NULL
);

CREATE TABLE product_costs (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(500) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE product_dimensions (
    height DECIMAL(10, 2) NOT NULL,
    width DECIMAL(10, 2) NOT NULL,
    depth DECIMAL(10, 2) NOT NULL,
    product_id BIGINT UNIQUE REFERENCES products(id) ON DELETE CASCADE
);

