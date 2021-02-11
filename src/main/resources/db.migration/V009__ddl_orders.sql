SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
    id                  INT(11)       NOT NULL AUTO_INCREMENT,
    user_id             INT(11)       NOT NULL,
    price               DECIMAL(8, 2) NOT NULL,
    delivery_price      DECIMAL(8, 2) NOT NULL,
    delivery_address_id INT(11)       NOT NULL,
    phone_number        VARCHAR(20)   NOT NULL,
    status_id           INT(11)       NOT NULL,
    delivery_date       TIMESTAMP     NOT NULL,
    create_at           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT FK_USER_ID FOREIGN KEY (user_id)
        REFERENCES users (id),
    CONSTRAINT FK_STATUS_ID FOREIGN KEY (status_id)
        REFERENCES orders_statuses (id),
    CONSTRAINT FK_DELIVERY_ADDRESS_ID FOREIGN KEY (delivery_address_id)
        REFERENCES delivery_addresses (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;