-- customers.credcards definition

-- Drop table

-- DROP TABLE customers.credcards;

create schema if not exists customers;

CREATE TABLE IF NOT EXISTS customers.credcards (
	id bigserial NOT NULL,
	number_card varchar(20) NOT NULL,
	valid date NOT NULL,
	cvv smallint NOT NULL,
    principal boolean NOT NULL,
	customer_id int8 NOT NULL,
	CONSTRAINT credcards_pkey PRIMARY KEY (id)
);


-- customers.credcards foreign keys

ALTER TABLE customers.credcards ADD CONSTRAINT credcards_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES customers.customer(id);
