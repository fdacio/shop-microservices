create table if not exists auth.user_rule(
    id bigserial primary key,
    user_id bigint not null REFERENCES auth.user(id),
    rule_id bigint not null REFERENCES auth.rule(id)
);