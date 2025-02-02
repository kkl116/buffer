CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    price NUMERIC NOT NULL
);

CREATE TABLE product_costs (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    price NUMERIC NOT NULL,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE product_dimensions (
    height NUMERIC NOT NULL,
    width NUMERIC NOT NULL,
    depth NUMERIC NOT NULL,
    product_id BIGINT UNIQUE REFERENCES products(id) ON DELETE CASCADE
);

