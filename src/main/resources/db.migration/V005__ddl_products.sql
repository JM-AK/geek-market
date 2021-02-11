SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS products;

CREATE TABLE products
(
    id                INT(11)       NOT NULL AUTO_INCREMENT,
    category_id       INT(11)       NOT NULL,
    vendor_code       VARCHAR(8)    NOT NULL,
    title             VARCHAR(255)  NOT NULL,
    short_description VARCHAR(1000) NOT NULL,
    full_description  VARCHAR(5000) NOT NULL,
    price             DECIMAL(8, 2) NOT NULL,
    create_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT FK_CATEGORY_ID FOREIGN KEY (category_id)
        REFERENCES categories (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;