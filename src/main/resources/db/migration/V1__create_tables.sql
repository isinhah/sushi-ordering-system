CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE phone (
    id SERIAL PRIMARY KEY,
    number VARCHAR(255) NOT NULL,
    customer_id UUID,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE addresses (
    id SERIAL PRIMARY KEY,
    number VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    customer_id UUID,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE payment (
    id SERIAL PRIMARY KEY,
    status INT NOT NULL
);

CREATE TABLE cashpayment (
    id SERIAL PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    payment_id BIGINT,
    FOREIGN KEY (payment_id) REFERENCES payment(id) ON DELETE CASCADE
);

CREATE TABLE pixpayment (
    id SERIAL PRIMARY KEY,
    pix_key VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    status VARCHAR(255) NOT NULL,
    payment_id BIGINT,
    FOREIGN KEY (payment_id) REFERENCES payment(id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    order_date TIMESTAMP NOT NULL,
    customer_id UUID NOT NULL,
    payment_id BIGINT NOT NULL,
    delivery_address_id INTEGER,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (payment_id) REFERENCES payment(id) ON DELETE CASCADE,
    FOREIGN KEY (delivery_address_id) REFERENCES addresses(id) ON DELETE CASCADE
);

CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    portion_quantity INTEGER NOT NULL,
    portion_unit VARCHAR(255) NOT NULL,
    url_image VARCHAR(255) NOT NULL
);

CREATE TABLE order_item (
    id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL,
    price DOUBLE PRECISION NOT NULL
);

CREATE TABLE category_product (
    category_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (category_id, product_id),
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
