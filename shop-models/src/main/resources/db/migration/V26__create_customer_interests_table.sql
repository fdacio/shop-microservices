create schema if not exists customers;
create table if not exists customers.interests(
    id bigserial primary key,
    customer_id bigint not null REFERENCES customers.customer(id),
    category_id bigint not null REFERENCES products.category(id)
);
