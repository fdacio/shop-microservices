insert into customers.customer(id, nome, cpf, endereco, email, telefone, data_cadastro)
select id, nome, cpf, endereco, email, telefone, data_cadastro from users.user;

insert into customers.interests(id, customer_id, category_id)
select id, user_id, category_id from users.interesses;