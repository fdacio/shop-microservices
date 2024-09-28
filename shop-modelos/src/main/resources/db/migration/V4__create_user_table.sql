create schema if not exists users;

create table if not exists users.user (
    id bigserial primary key,
    nome varchar(80) not null,
    cpf varchar(11) not null,
    endereco varchar(100) not null,
    email varchar(100) not null,
    telefone varchar(20) not null,
    data_cadastro timestamp not null
)
