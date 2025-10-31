-- orders.orders definition

-- Drop table

-- DROP TABLE orders.orders;

create schema if not exists orders;

CREATE TABLE IF NOT EXISTS orders.orders (
	id bigserial NOT NULL,
	date_order timestamp NOT NULL,
	total numeric(15, 2) NOT NULL,
	customer_id int8 NULL,
	CONSTRAINT order_pkey PRIMARY KEY (id)
);


-- orders.orders foreign keys

ALTER TABLE orders.orders ADD CONSTRAINT fk_order_customers FOREIGN KEY (customer_id) REFERENCES customers.customer(id);