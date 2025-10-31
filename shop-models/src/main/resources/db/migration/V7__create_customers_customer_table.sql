-- customers.customer definition

-- Drop table

-- DROP TABLE customers.customer;

create schema if not exists customers;

CREATE TABLE IF NOT EXISTS customers.customer (
	id bigserial NOT NULL,
	nome varchar(80) NOT NULL,
	cpf varchar(11) NOT NULL,
	endereco varchar(100) NOT NULL,
	email varchar(100) NOT NULL,
	telefone varchar(20) NOT NULL,
	key_auth varchar(255) NULL,
	data_cadastro timestamp NULL,
	CONSTRAINT customer_pkey PRIMARY KEY (id)
);