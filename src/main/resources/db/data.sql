INSERT INTO products (name, description, price) VALUES
('Dummy 1', 'Description 1', 5.00),
('Dummy 2', 'Description 2', 2.00);

INSERT INTO product_costs (product_id, name, price) VALUES
(1, 'dummy cost', 2.00);

INSERT INTO product_dimensions (product_id, height, width, depth) VALUES
(1, 8.00, 24.00, 0.00),
(2, 8.00, 24.00, 0.00);