create schema if not exists products;

create table if not exists products.category(
    id bigserial primary key,
    nome varchar(30) not null
);
