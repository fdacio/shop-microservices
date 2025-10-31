CREATE TABLE IF NOT EXISTS auth."user" (
	id bigserial NOT NULL,
	nome varchar(60) NOT NULL,
	username varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	key_token varchar(255) NOT NULL,
	hash_recovery_password varchar NULL,
	data_cadastro timestamp NOT NULL,
	CONSTRAINT user_email_key UNIQUE (email),
	CONSTRAINT user_key_token_key UNIQUE (key_token),
	CONSTRAINT user_pkey PRIMARY KEY (id),
	CONSTRAINT user_username_key UNIQUE (username)
);