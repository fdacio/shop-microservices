set search_path to shopping;
alter table if exists shop rename to orders;
alter table if exists itens rename column shop_id TO order_id;
alter schema shopping rename to orders;
