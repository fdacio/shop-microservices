alter table customers.customer add column tmp timestamp;
update customers.customer set tmp = data_cadastro;
alter table customers.customer drop column data_cadastro;
alter table customers.customer add column key_auth varchar(255) null;
alter table customers.customer add column data_cadastro timestamp;
update customers.customer set data_cadastro = tmp;
alter table customers.customer drop column tmp;
