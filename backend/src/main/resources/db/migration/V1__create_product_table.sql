DROP TABLE products IF EXISTS;

CREATE TABLE products (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    title VARCHAR(511) NOT NULL,

    description TEXT DEFAULT '',

    thumbnailUrl VARCHAR(511) NOT NULL,

    price DECIMAL(10, 2) NOT NULL
);
