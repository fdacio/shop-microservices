create table if not exists users.interesses(
    id bigserial primary key,
    user_id bigint not null REFERENCES users.user(id),
    category_id bigint not null REFERENCES products.category(id)
);
