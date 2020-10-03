DROP TABLE product_categories IF EXISTS;

CREATE TABLE product_categories (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    parent_category BIGINT,

    FOREIGN KEY (parent_category) REFERENCES product_categories(id)
);
