CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE phone (
    id BIGINT PRIMARY KEY,
    number VARCHAR(255),
    customer_id UUID,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE addresses (
    id BIGINT PRIMARY KEY,
    number VARCHAR(255),
    street VARCHAR(255),
    neighborhood VARCHAR(255),
    customer_id UUID,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE payment (
    id BIGINT PRIMARY KEY,
    status INT
);

CREATE TABLE cashpayment (
    id BIGINT PRIMARY KEY,
    amount DOUBLE PRECISION,
    payment_id BIGINT,
    FOREIGN KEY (payment_id) REFERENCES payment(id) ON DELETE CASCADE
);

CREATE TABLE pixpayment (
    id BIGINT PRIMARY KEY,
    pix_key VARCHAR(255),
    amount DOUBLE PRECISION,
    status VARCHAR(255),
    payment_id BIGINT,
    FOREIGN KEY (payment_id) REFERENCES payment(id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id BIGINT PRIMARY KEY,
    instant_order TIMESTAMP,
    customer_id UUID,
    payment_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (payment_id) REFERENCES payment(id) ON DELETE CASCADE
);

CREATE TABLE products (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    price DOUBLE PRECISION
);

CREATE TABLE menu (
    id BIGINT PRIMARY KEY,
    product_name VARCHAR(255),
    description TEXT,
    price DOUBLE PRECISION,
    available BOOLEAN,
    product_img VARCHAR(255)
);

CREATE TABLE categories (
    id BIGINT PRIMARY KEY,
    quantity INT,
    price DOUBLE PRECISION,
    menu_id BIGINT,
    FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE
);