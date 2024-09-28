create schema if not exists users;

create table if not exists users.rule(
    id bigserial primary key,
    nome varchar(30) not null
);
