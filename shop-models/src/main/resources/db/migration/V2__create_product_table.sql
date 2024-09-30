create schema if not exists products;

create table if not exists products.product(
    id bigserial primary key,
    product_identifier varchar(20) not null,
    nome varchar(60) not null,
    descricao varchar(120) not null,
    preco numeric(15,2) not null,
    category_id bigint not null REFERENCES products.category(id)
);
