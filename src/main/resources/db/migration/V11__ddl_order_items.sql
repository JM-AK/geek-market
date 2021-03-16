SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS order_items;

CREATE TABLE order_items
(
    id          INT(11)       NOT NULL AUTO_INCREMENT,
    product_id  INT(11)       NOT NULL,
    order_id    INT(11)       NOT NULL,
    quantity    INT           NOT NULL,
    item_price  DECIMAL(8, 2) NOT NULL,
    total_price DECIMAL(8, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_ORDER_ID FOREIGN KEY (order_id)
        REFERENCES orders (id),
    CONSTRAINT FK_PRODUCT_ID_ORD_IT FOREIGN KEY (product_id)
        REFERENCES products (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;