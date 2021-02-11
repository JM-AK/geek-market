SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS categories;

CREATE TABLE categories
(
    id          INT(11)      NOT NULL AUTO_INCREMENT,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(5000),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
