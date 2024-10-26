alter table if exists shopping.shop rename to shopping.order;
ALTER TABLE shopping.itens RENAME COLUMN shop_id TO order_it;
alter schema if exists shopping rename to orders;
