-- products.product definition

-- Drop table

-- DROP TABLE products.product;

CREATE TABLE IF NOT EXISTS products.product (
	id bigserial NOT NULL,
	identifier varchar(20) NOT NULL,
	nome varchar(120) NOT NULL,
	descricao varchar(240) NOT NULL,
	preco numeric(15, 2) NOT NULL,
	foto varchar(250) NULL,
	category_id int8 NULL,
	CONSTRAINT product_pkey PRIMARY KEY (id)
);


-- products.product foreign keys

ALTER TABLE products.product ADD CONSTRAINT product_category_id_fkey FOREIGN KEY (category_id) REFERENCES products.category(id);