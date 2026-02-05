begin;

-- Temporary field for set customers id persist in database
alter table orders.orders add customer_id_tmp int8;

-- Set customer id from customer_id for temporary filed customer id
update orders.orders set customer_id_tmp = customer_id;

-- Alter table orders removing the foreign key customer id and your constraint
alter table orders.orders drop constraint fk_order_customers;
alter table orders.orders drop column customer_id;

-- Alter table orders adding a new field status, that set possible status from the a order
alter table orders.orders add column status int4 not null;

alter table orders.orders add column customer_id int8 not null;

update orders.orders set customer_id = customer_id_tmp;

ALTER TABLE orders.orders ADD CONSTRAINT fk_order_customers FOREIGN KEY (customer_id) REFERENCES customers.customer(id);


alter table orders.orders drop column customer_id_tmp;


commit;