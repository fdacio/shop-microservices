create schema if not exists auth;

create table if not exists auth.config(
    id bigserial primary key,
    chave varchar(30) not null,
    valor varchar (255) not null
);
