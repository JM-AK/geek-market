SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS products_images;

CREATE TABLE products_images
(
    id         INT(11)      NOT NULL AUTO_INCREMENT,
    product_id INT(11)      NOT NULL,
    path       VARCHAR(250) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_PRODUCT_ID_IMG FOREIGN KEY (product_id)
        REFERENCES products (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;