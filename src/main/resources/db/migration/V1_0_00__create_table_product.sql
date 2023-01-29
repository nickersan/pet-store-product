CREATE SEQUENCE IF NOT EXISTS product_id;

CREATE TABLE IF NOT EXISTS product
(
    product_id   INT           NOT NULL PRIMARY KEY,
    product_name VARCHAR(200)  NOT NULL,
    sku          VARCHAR(20)   NOT NULL,
    description  VARCHAR(2000) NOT NULL,
    price        DECIMAL       NOT NULL
);