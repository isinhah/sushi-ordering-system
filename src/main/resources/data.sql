-- Habilitar a extensão uuid-ossp para gerar UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Inserir customers. (Senha = 123)
INSERT INTO customers (id, name, email, password) VALUES
(uuid_generate_v4(), 'Ana', 'ana@gmail.com', '$2a$10$6f0rWHMXsne13lwsQ2V48.PhBkFzfCZzWkIJ/qWD0QLAshq88HOOW'),
(uuid_generate_v4(), 'Carlos', 'carlos@gmail.com', '$2a$10$6f0rWHMXsne13lwsQ2V48.PhBkFzfCZzWkIJ/qWD0QLAshq88HOOW'),
(uuid_generate_v4(), 'Fernanda', 'fernanda@gmail.com', '$2a$10$6f0rWHMXsne13lwsQ2V48.PhBkFzfCZzWkIJ/qWD0QLAshq88HOOW'),
(uuid_generate_v4(), 'Gustavo', 'gustavo@gmail.com', '$2a$10$6f0rWHMXsne13lwsQ2V48.PhBkFzfCZzWkIJ/qWD0QLAshq88HOOW'),
(uuid_generate_v4(), 'Juliana', 'juliana@gmail.com', '$2a$10$6f0rWHMXsne13lwsQ2V48.PhBkFzfCZzWkIJ/qWD0QLAshq88HOOW');

-- Inserir employees
INSERT INTO employees (id, name, email, password) VALUES
(uuid_generate_v4(), 'Isabel', 'isabel@gmail.com', '$2a$10$6f0rWHMXsne13lwsQ2V48.PhBkFzfCZzWkIJ/qWD0QLAshq88HOOW'),
(uuid_generate_v4(), 'Giulia', 'giulia@gmail.com', '$2a$10$6f0rWHMXsne13lwsQ2V48.PhBkFzfCZzWkIJ/qWD0QLAshq88HOOW'),
(uuid_generate_v4(), 'Maria', 'maria@gmail.com', '$2a$10$6f0rWHMXsne13lwsQ2V48.PhBkFzfCZzWkIJ/qWD0QLAshq88HOOW'),
(uuid_generate_v4(), 'João', 'joao@gmail.com', '$2a$10$6f0rWHMXsne13lwsQ2V48.PhBkFzfCZzWkIJ/qWD0QLAshq88HOOW');

-- Inserir phones
INSERT INTO phone (number, customer_id) VALUES
('123456789', (SELECT id FROM customers WHERE name = 'Ana')),
('987654321', (SELECT id FROM customers WHERE name = 'Carlos')),
('555555555', (SELECT id FROM customers WHERE name = 'Fernanda')),
('444444444', (SELECT id FROM customers WHERE name = 'Gustavo')),
('333333333', (SELECT id FROM customers WHERE name = 'Juliana'));

-- Inserir addresses
INSERT INTO addresses (number, street, neighborhood, customer_id) VALUES
('123', 'Rua das Flores', 'Centro', (SELECT id FROM customers WHERE name = 'Ana')),
('456', 'Avenida Brasil', 'Jardim', (SELECT id FROM customers WHERE name = 'Carlos')),
('789', 'Rua das Palmeiras', 'Vila Nova', (SELECT id FROM customers WHERE name = 'Fernanda')),
('101', 'Praça Central', 'Bela Vista', (SELECT id FROM customers WHERE name = 'Gustavo')),
('202', 'Rua do Comércio', 'Santa Cruz', (SELECT id FROM customers WHERE name = 'Juliana'));

-- Inserir categories
INSERT INTO categories (name, description) VALUES
('Sushi', 'Various types of sushi'),
('Japanese Food', 'Japanese cuisine dishes'),
('Chinese Food', 'Chinese cuisine dishes'),
('Beverages', 'Various types of drinks');

-- Inserir products
INSERT INTO products (name, description, price, portion_quantity, portion_unit, url_image) VALUES
('California Roll', 'A delicious roll made with crab meat, avocado, and cucumber', 8.99, 8, 'pieces', 'http://example.com/images/california_roll.jpg'),
('Spicy Tuna Roll', 'Spicy tuna wrapped in rice and seaweed', 10.99, 8, 'pieces', 'http://example.com/images/spicy_tuna_roll.jpg'),
('Sushi Assortment', 'A variety of sushi pieces including nigiri and sashimi', 15.99, 10, 'pieces', 'http://example.com/images/sushi_assortment.jpg'),
('Tempura Shrimp', 'Crispy tempura shrimp served with dipping sauce', 12.99, 8, 'pieces', 'http://example.com/images/tempura_shrimp.jpg'),
('Miso Soup', 'Traditional Japanese miso soup', 5.99, 1, 'bowl', 'http://example.com/images/miso_soup.jpg'),
('Peking Duck', 'Crispy duck served with pancakes and hoisin sauce', 18.99, 1, 'whole', 'http://example.com/images/peking_duck.jpg'),
('Spring Rolls', 'Crispy spring rolls with vegetables', 7.99, 6, 'pieces', 'http://example.com/images/spring_rolls.jpg'),
('Kung Pao Chicken', 'Spicy stir-fried chicken with peanuts', 13.99, 1, 'plate', 'http://example.com/images/kung_pao_chicken.jpg'),
('Green Tea', 'Traditional Japanese green tea', 3.99, 1, 'cup', 'http://example.com/images/green_tea.jpg'),
('Sake', 'Japanese rice wine', 14.99, 1, 'bottle', 'http://example.com/images/sake.jpg');

-- Inserir relacionamento category_product
INSERT INTO category_product (category_id, product_id) VALUES
((SELECT id FROM categories WHERE name = 'Sushi'), (SELECT id FROM products WHERE name = 'California Roll')),
((SELECT id FROM categories WHERE name = 'Sushi'), (SELECT id FROM products WHERE name = 'Spicy Tuna Roll')),
((SELECT id FROM categories WHERE name = 'Sushi'), (SELECT id FROM products WHERE name = 'Sushi Assortment')),
((SELECT id FROM categories WHERE name = 'Sushi'), (SELECT id FROM products WHERE name = 'Tempura Shrimp')),
((SELECT id FROM categories WHERE name = 'Japanese Food'), (SELECT id FROM products WHERE name = 'Miso Soup')),
((SELECT id FROM categories WHERE name = 'Chinese Food'), (SELECT id FROM products WHERE name = 'Peking Duck')),
((SELECT id FROM categories WHERE name = 'Chinese Food'), (SELECT id FROM products WHERE name = 'Spring Rolls')),
((SELECT id FROM categories WHERE name = 'Chinese Food'), (SELECT id FROM products WHERE name = 'Kung Pao Chicken')),
((SELECT id FROM categories WHERE name = 'Beverages'), (SELECT id FROM products WHERE name = 'Green Tea')),
((SELECT id FROM categories WHERE name = 'Beverages'), (SELECT id FROM products WHERE name = 'Sake'));

-- Inserir orders
INSERT INTO orders (order_date, customer_id, delivery_address_id, total_amount) VALUES
(NOW(), (SELECT id FROM customers WHERE name = 'Ana'), (SELECT id FROM addresses WHERE customer_id = (SELECT id FROM customers WHERE name = 'Ana')), 50.00),
(NOW(), (SELECT id FROM customers WHERE name = 'Carlos'), (SELECT id FROM addresses WHERE customer_id = (SELECT id FROM customers WHERE name = 'Carlos')), 35.00),
(NOW(), (SELECT id FROM customers WHERE name = 'Fernanda'), (SELECT id FROM addresses WHERE customer_id = (SELECT id FROM customers WHERE name = 'Fernanda')), 40.00),
(NOW(), (SELECT id FROM customers WHERE name = 'Gustavo'), (SELECT id FROM addresses WHERE customer_id = (SELECT id FROM customers WHERE name = 'Gustavo')), 55.00),
(NOW(), (SELECT id FROM customers WHERE name = 'Juliana'), (SELECT id FROM addresses WHERE customer_id = (SELECT id FROM customers WHERE name = 'Juliana')), 30.00);

-- Inserir order_item
INSERT INTO order_item (order_id, product_id, quantity, price, total_price) VALUES
((SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE name = 'Ana')), (SELECT id FROM products WHERE name = 'California Roll'), 2, 8.99, 17.98),
((SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE name = 'Ana')), (SELECT id FROM products WHERE name = 'Spicy Tuna Roll'), 1, 10.99, 10.99),
((SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE name = 'Carlos')), (SELECT id FROM products WHERE name = 'Tempura Shrimp'), 1, 12.99, 12.99),
((SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE name = 'Carlos')), (SELECT id FROM products WHERE name = 'Green Tea'), 2, 3.99, 7.98),
((SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE name = 'Fernanda')), (SELECT id FROM products WHERE name = 'Sushi Assortment'), 1, 15.99, 15.99),
((SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE name = 'Fernanda')), (SELECT id FROM products WHERE name = 'Kung Pao Chicken'), 1, 13.99, 13.99),
((SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE name = 'Gustavo')), (SELECT id FROM products WHERE name = 'Peking Duck'), 1, 18.99, 18.99),
((SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE name = 'Gustavo')), (SELECT id FROM products WHERE name = 'Sake'), 1, 14.99, 14.99),
((SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE name = 'Juliana')), (SELECT id FROM products WHERE name = 'Spring Rolls'), 1, 7.99, 7.99),
((SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE name = 'Juliana')), (SELECT id FROM products WHERE name = 'Green Tea'), 1, 3.99, 3.99);