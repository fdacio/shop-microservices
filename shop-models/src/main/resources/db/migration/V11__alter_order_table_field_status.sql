begin;

-- Temporary field for set customers id persist in database
alter table orders.orders add customer_id_tmp int8;

-- Set customer id from customer_id for temporary filed customer id
update orders.orders set customer_id_tmp = customer_id;

-- Alter table orders removing the foreign key customer id and your constraint
alter table orders.orders drop constraint fk_order_customers;
alter table orders.orders drop column customer_id;

-- Alter table orders adding a new field status, that set possible status from the a order
alter table orders.orders add column status int4 default 0 null;

alter table orders.orders add column customer_id int8 null;

update orders.orders set customer_id = customer_id_tmp;

alter table orders.orders alter column customer_id set not null;

update orders.orders set status = 0 where status is null;

alter table orders.orders alter column status set not null;

alter table orders.orders add constraint fk_order_customers foreign key (customer_id) references customers.customer(id);

alter table orders.orders drop column customer_id_tmp;

commit;