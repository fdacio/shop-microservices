set search_path to shopping;
alter table shop rename to orders;
search_path itens rename column shop_id TO order_id;
alter schema shopping rename to orders;
