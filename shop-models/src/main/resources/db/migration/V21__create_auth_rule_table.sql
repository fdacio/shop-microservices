create schema if not exists auth;

create table if not exists auth.rule(
    id bigserial primary key,
    nome varchar(30) not null
);
