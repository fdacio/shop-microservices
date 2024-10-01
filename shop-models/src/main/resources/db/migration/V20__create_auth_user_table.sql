create schema if not exists auth;

create table if not exists auth.user (
    id bigserial primary key,
    nome varchar(100) not null,
    username varchar(255) not null unique,
    password varchar(255) not null,
    email varchar(255) not null unique,
    hash_recovery_password varchar null,
    data_cadastro timestamp not null
)
