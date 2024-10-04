create schema if not exists auth;
drop table if exists auth.user_rule;
drop table if exists auth.user;

create table if not exists auth.user (
    id bigserial primary key,
    nome varchar(60) not null,
    username varchar(255) not null unique,
    password varchar(255) not null,
    email varchar(255) not null unique,
    key_token varchar(255) not null unique,
    hash_recovery_password varchar null,
    data_cadastro timestamp not null
);

create table if not exists auth.user_rule(
    id bigserial primary key,
    user_id bigint not null REFERENCES auth.user(id),
    rule_id bigint not null REFERENCES auth.rule(id)
);