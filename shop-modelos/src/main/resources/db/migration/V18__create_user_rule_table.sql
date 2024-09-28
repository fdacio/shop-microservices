create table if not exists users.user_rule(
    id bigserial primary key,
    user_id bigint not null REFERENCES users.user(id),
    rule_id bigint not null REFERENCES users.rule(id)
);