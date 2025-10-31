CREATE TABLE IF NOT EXISTS auth."rule" (
	id bigserial NOT NULL,
	nome varchar(30) NOT NULL,
	CONSTRAINT rule_pkey PRIMARY KEY (id)
);