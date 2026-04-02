-- orders.orders_payments definition

-- Drop table

-- DROP TABLE orders.orders_payments;

CREATE TABLE IF NOT EXISTS orders.orders_payments (
    id bigserial NOT NULL,
    date_payment timestamp NOT NULL,
    status int4 NOT NULL,
    message varchar(255) NOT NULL,
    order_id int8 NOT NULL,
    credcard_id int8 NOT NULL,
    CONSTRAINT orders_payments_pkey PRIMARY KEY (id)
);

-- orders.orders_payments foreign keys
ALTER TABLE orders.orders_payments ADD CONSTRAINT orders_payments_order_id_fkey FOREIGN KEY (order_id) REFERENCES orders.orders(id);
ALTER TABLE orders.orders_payments ADD CONSTRAINT orders_payments_credcard_id_fkey FOREIGN KEY (credcard_id) REFERENCES customers.credcards(id);
