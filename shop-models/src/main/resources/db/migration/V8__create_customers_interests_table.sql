-- customers.interests definition

-- Drop table

-- DROP TABLE customers.interests;

CREATE TABLE IF NOT EXISTS customers.interests (
	id bigserial NOT NULL,
	customer_id int8 NOT NULL,
	category_id int8 NOT NULL,
	CONSTRAINT interests_pkey PRIMARY KEY (id)
);


-- customers.interests foreign keys

ALTER TABLE customers.interests ADD CONSTRAINT interests_category_id_fkey FOREIGN KEY (category_id) REFERENCES products.category(id);
ALTER TABLE customers.interests ADD CONSTRAINT interests_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES customers.customer(id);