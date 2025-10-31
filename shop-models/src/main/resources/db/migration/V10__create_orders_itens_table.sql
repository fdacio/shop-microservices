-- orders.itens definition

-- Drop table

-- DROP TABLE orders.itens;

CREATE TABLE IF NOT EXISTS orders.itens (
	id bigserial NOT NULL,
	quantidade int4 NOT NULL,
	preco numeric(15, 2) NOT NULL,
	order_id int8 NOT NULL,
	product_id int8 NOT NULL,
	CONSTRAINT itens_pkey PRIMARY KEY (id)
);


-- orders.itens foreign keys

ALTER TABLE orders.itens ADD CONSTRAINT itens_product_id_fkey FOREIGN KEY (product_id) REFERENCES products.product(id);
ALTER TABLE orders.itens ADD CONSTRAINT itens_shop_id_fkey FOREIGN KEY (order_id) REFERENCES orders.orders(id);