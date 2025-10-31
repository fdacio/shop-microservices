-- auth.config definition

-- Drop table

-- DROP TABLE auth.config;
create schema if not exists auth;

CREATE TABLE IF NOT EXISTS auth.config (
	id bigserial NOT NULL,
	chave varchar(30) NOT NULL,
	valor varchar(255) NOT NULL,
	CONSTRAINT config_pkey PRIMARY KEY (id)
);