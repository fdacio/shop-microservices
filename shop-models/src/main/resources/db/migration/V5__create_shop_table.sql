create schema if not exists shopping;

create table if not exists shopping.shop (
    
    id bigserial primary key,
    data timestamp not null,
    total numeric(15,2) not null,
    user_id bigint not null REFERENCES users.user(id)

);

create table if not exists shopping.itens (
    id bigserial primary key,
    quantidade int not null,
    preco numeric(15,2) not null,
    shop_id bigint not null REFERENCES shopping.shop(id),
    product_id bigint not null REFERENCES products.product(id)
);
