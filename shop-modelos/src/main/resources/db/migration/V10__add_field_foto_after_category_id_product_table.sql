alter table products.product add column tmp bigint;
update products.product set tmp = category_id;
alter table products.product drop column foto;
alter table products.product drop column category_id;
alter table products.product add column foto varchar(250);
alter table products.product add column category_id bigint;
update products.product set category_id = tmp;
alter table products.product drop column tmp;
alter table products.product add constraint product_category_id_fkey foreign key (category_id) references products.category(id);