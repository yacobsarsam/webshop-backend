CREATE TABLE category
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE product
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    price DOUBLE NOT NULL,
    quantity      INT    NOT NULL,
    picture_path  VARCHAR(255) NULL,
    category_id   BIGINT NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE user
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    email    VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    `role`   VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);