SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS products_categories;

CREATE TABLE products_categories
(
    product_id INT(11) NOT NULL,
    category_id INT(11) NOT NULL,

    PRIMARY KEY (product_id, category_id),

    CONSTRAINT FK_PRODUCT_ID_01 FOREIGN KEY (product_id)
        REFERENCES products (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,

    CONSTRAINT FK_CATEGORY_ID FOREIGN KEY (category_id)
        REFERENCES categories (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
