CREATE SEQUENCE product_id;

CREATE TABLE product
(
    product_id   NUMBER         NOT NULL PRIMARY KEY,
    product_name VARCHAR2(200)  NOT NULL,
    sku          VARCHAR2(20)   NOT NULL,
    description  VARCHAR2(2000) NOT NULL,
    price        NUMBER         NOT NULL
);