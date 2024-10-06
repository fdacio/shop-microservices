alter table shopping.shop add column customer_id bigint;
update shopping.shop set customer_id = user_id;
alter table shopping.shop add constraint fk_shop_customers foreign key (customer_id) references customers.customer (id);
alter table shopping.shop drop column user_id;


