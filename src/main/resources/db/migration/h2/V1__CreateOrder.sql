DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
  id                  UUID PRIMARY KEY NOT NULL,
  account_id          UUID             NOT NULL,
  payment_provider_id UUID             NOT NULL,
  state               VARCHAR(255)     NOT NULL,
  created_on          TIMESTAMP        NOT NULL DEFAULT now()
);

DROP TABLE IF EXISTS item;

CREATE TABLE item
(
  id         UUID PRIMARY KEY NOT NULL,
  product_id UUID             NOT NULL,
  order_id   UUID             NOT NULL,
  name       VARCHAR(255)     NOT NULL,
  quantity   INTEGER          NOT NULL,
  price      NUMERIC(19, 2)   NOT NULL,
  created_on TIMESTAMP        NOT NULL DEFAULT now()
);