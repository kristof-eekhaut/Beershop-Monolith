DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
  id                  UUID PRIMARY KEY NOT NULL,
  account_id          UUID             NOT NULL,
  payment_provider_id UUID             NOT NULL,
  state               VARCHAR(255)     NOT NULL,
  created_on          TIMESTAMP        NOT NULL DEFAULT now()
);

DROP TABLE IF EXISTS product;

CREATE TABLE product
(
  id         UUID PRIMARY KEY NOT NULL,
  name       VARCHAR(255)     NOT NULL,
  quantity   INTEGER          NOT NULL,
  price      NUMERIC(19, 2)   NOT NULL,
  created_on TIMESTAMP        NOT NULL DEFAULT now()
);

DROP TABLE IF EXISTS order_product;

CREATE TABLE order_product
(
  id          UUID PRIMARY KEY NOT NULL,
  orderId     UUID             NOT NULL,
  productId   UUID             NOT NULL,
  quantity    INTEGER          NOT NULL,
  total_price NUMERIC(19, 2)   NOT NULL,
  created_on  TIMESTAMP        NOT NULL DEFAULT now(),
  FOREIGN KEY (orderId) REFERENCES orders (id),
  FOREIGN KEY (productId) REFERENCES product (id)
)