SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS delivery_addresses;

CREATE TABLE delivery_addresses
(
    id      INT(11)      NOT NULL AUTO_INCREMENT,
    user_id INT(11)      NOT NULL,
    address VARCHAR(500) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_USER_ID_DEL_ADR FOREIGN KEY (user_id)
        REFERENCES users (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;