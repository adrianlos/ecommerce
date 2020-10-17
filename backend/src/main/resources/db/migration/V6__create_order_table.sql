DROP TABLE IF EXISTS orders;

CREATE TABLE orders (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    customer BIGINT NOT NULL,

    customer_country VARCHAR(64) NOT NULL,

    customer_city VARCHAR(64) NOT NULL,

    customer_street VARCHAR(64) NOT NULL,

    customer_zip_code VARCHAR(16) NOT NULL,

    delivery_country VARCHAR(64) NOT NULL,

    delivery_city VARCHAR(64) NOT NULL,

    delivery_street VARCHAR(64) NOT NULL,

    delivery_zip_code VARCHAR(16) NOT NULL,

    order_time DATETIME NOT NULL,

    total_cost DECIMAL(10, 2) NOT NULL,

    state VARCHAR(11) NOT NULL,

    FOREIGN KEY (customer) REFERENCES users(id)
);

DROP TABLE IF EXISTS order_entries;

CREATE TABLE order_entries (

   product BIGINT NOT NULL,

   price DECIMAL(10, 2) NOT NULL,

   count DECIMAL(6, 0) NOT NULL,

   FOREIGN KEY (product) REFERENCES products(id)
);

