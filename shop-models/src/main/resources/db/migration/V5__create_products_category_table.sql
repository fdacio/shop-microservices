-- products.category definition

-- Drop table

-- DROP TABLE products.category;

create schema if not exists products;

CREATE TABLE IF NOT EXISTS products.category (
	id bigserial NOT NULL,
	nome varchar(30) NOT NULL,
	CONSTRAINT category_pkey PRIMARY KEY (id)
);