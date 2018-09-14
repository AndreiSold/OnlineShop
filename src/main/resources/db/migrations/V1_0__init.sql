-- create table customer (
--   id integer not null,
--   first_name varchar(255),
--   last_name varchar(255),
--   username varchar(255),
--   primary key (id)
-- ) engine=MyISAM;
--
-- create table hibernate_sequence (
--   next_val bigint
-- ) engine=MyISAM;
--
-- insert into hibernate_sequence values ( 1001 );
-- insert into hibernate_sequence values ( 1001 );
-- insert into hibernate_sequence values ( 1001 );
-- insert into hibernate_sequence values ( 1001 );
-- insert into hibernate_sequence values ( 1001 );
-- insert into hibernate_sequence values ( 1001 );
-- insert into hibernate_sequence values ( 1001 );
-- insert into hibernate_sequence values ( 1001 );
--
-- create table location (
--   id integer not null,
--   city varchar(255),
--   country varchar(255),
--   county varchar(255),
--   street_address varchar(255),
--   name varchar(255),
--   primary key (id)
-- ) engine=MyISAM;
--
-- create table order_ (
--   id integer not null,
--   city varchar(255),
--   country varchar(255),
--   county varchar(255),
--   street_address varchar(255),
--   customer_id integer,
--   primary key (id)
-- ) engine=MyISAM;
--
-- create table order_detail (
--   id integer not null,
--   quantity integer,
--   order_id integer,
--   product_id integer,
--   primary key (id)
-- ) engine=MyISAM;
--
-- create table orders_locations (
--   location_id integer not null,
--   order_id integer not null
-- ) engine=MyISAM;
--
-- create table product (
--   id integer not null,
--   description varchar(255),
--   name varchar(255),
--   price decimal(19,2),
--   weight double precision,
--   primary key (id)
-- ) engine=MyISAM;
--
-- create table product_category (
--   id integer not null,
--   description varchar(255),
--   name varchar(255),
--   primary key (id)
-- ) engine=MyISAM;
--
-- create table products_categories (
--   product_category_id integer not null,
--   product_id integer not null
-- ) engine=MyISAM;
--
-- create table products_suppliers (
--   supplier_id integer not null,
--   product_id integer not null,
--   constraint sdf unique (supplier_id, product_id)
--
-- ) engine=MyISAM;
--
--
-- create table stock (
--   id integer not null,
--   quantity integer,
--   location_id integer,
--   product_id integer,
--   primary key (id)
-- ) engine=MyISAM;
--
-- create table supplier (
--   id integer not null,
--   name varchar(255),
--   primary key (id)
-- ) engine=MyISAM;
--
-- alter table order_ add constraint FK_CUSTOMER_FROM_ORDER foreign key (customer_id) references customer (id);
-- alter table order_detail add constraint FK_ORDER_FROM_ORDER_DETAIL foreign key (order_id) references order_ (id);
-- alter table order_detail add constraint FK_PRODUCT_FROM_ORDER_DETAIL foreign key (product_id) references product (id);
-- alter table orders_locations add constraint FK_ORDER_FROM_ORDERS_LOCATIONS foreign key (order_id) references order_ (id);
-- alter table orders_locations add constraint FK_LOCATION_FROM_ORDERS_LOCATIONS foreign key (location_id) references location (id);
-- alter table products_categories add constraint FK_PRODUCT_FROM_PRODUCTS_CATEGORIES foreign key (product_id) references product (id);
-- alter table products_categories add constraint FK_PRODUCT_CATEGORY_FROM_PRODUCTS_CATEGORIES foreign key (product_category_id) references product_category (id);
-- alter table products_suppliers add constraint FK_PRODUCT_FROM_PRODUCTS_SUPPLIERS foreign key (product_id) references product (id);
-- alter table products_suppliers add constraint FK_SUPPLIER_FROM_PRODUCTS_SUPPLIERS foreign key (supplier_id) references supplier (id);
-- alter table stock add constraint FK_LOCATION_FROM_STOCK foreign key (location_id) references location (id);


create table customer (
  id integer not null,
  first_name varchar(255),
  last_name varchar(255),
  username varchar(255),
  primary key (id)
) engine=InnoDB;

create table hibernate_sequence (
  next_val bigint
) engine=InnoDB;

insert into hibernate_sequence values ( 1001 );
insert into hibernate_sequence values ( 1001 );
insert into hibernate_sequence values ( 1001 );
insert into hibernate_sequence values ( 1001 );
insert into hibernate_sequence values ( 1001 );
insert into hibernate_sequence values ( 1001 );
insert into hibernate_sequence values ( 1001 );
insert into hibernate_sequence values ( 1001 );

create table location (
  id integer not null,
  city varchar(255),
  country varchar(255),
  county varchar(255),
  street_address varchar(255),
  name varchar(255),
  primary key (id)
) engine=InnoDB;

create table order_ (
  id integer not null,
  city varchar(255),
  country varchar(255),
  county varchar(255),
  street_address varchar(255),
  customer_id integer,
  primary key (id)
) engine=InnoDB;

create table order__shipped_from (
  orders_id integer not null,
  shipped_from_id integer not null
) engine=InnoDB;

create table order_detail (
  id integer not null,
  quantity integer,
  order_id integer,
  product_id integer,
  primary key (id)
) engine=InnoDB;

create table product (
  id integer not null,
  description varchar(255),
  name varchar(255),
  price double precision,
  weight double precision,
  primary key (id)
) engine=InnoDB;

create table product_category (
  id integer not null,
  description varchar(255),
  name varchar(255),
  primary key (id)
) engine=InnoDB;

create table product_category_products (
  product_categories_id integer not null,
  products_id integer not null
) engine=InnoDB;

create table stock (
  id integer not null,
  quantity integer,
  location_id integer,
  product_id integer,
  primary key (id)
) engine=InnoDB;

create table supplier (
  id integer not null,
  name varchar(255),
  primary key (id)
) engine=InnoDB;

create table supplier_products (
  suppliers_id integer not null,
  products_id integer not null
) engine=InnoDB;

alter table order_ add constraint FK_CUSTOMER_FROM_ORDER foreign key (customer_id) references customer (id);
alter table order__shipped_from add constraint FK_SHIPPED_FROM_FROM_ORDER_SHIPPED_FROM foreign key (shipped_from_id) references location (id);
alter table order__shipped_from add constraint FK_ORDER_FROM_ORDER_SHIPPED_FROM foreign key (orders_id) references order_ (id);
alter table order_detail add constraint FK_ORDER_FROM_ORDER_DETAIL foreign key (order_id) references order_ (id);
alter table order_detail add constraint FK_PRODUCT_FROM_ORDER_DETAIL foreign key (product_id) references product (id);
alter table product_category_products add constraint FK_PRODUCT_FROM_PRODUCT_CATEGORY_PRODUCTS foreign key (products_id) references product (id);
alter table product_category_products add constraint FK_PRODUCT_CATEGORY_FROM_PRODUCT_CATEGORY_PRODUCTS foreign key (product_categories_id) references product_category (id);
alter table stock add constraint FK_LOCATION_FROM_STOCK foreign key (location_id) references location (id);
alter table stock add constraint FK_PRODUCT_FROM_STOCK foreign key (product_id) references product (id);
alter table supplier_products add constraint FK_PRODUCT_FROM_SUPPLIER_PRODUCTS foreign key (products_id) references product (id);
alter table supplier_products add constraint FK_SUPPLIER_FROM_SUPPLIER_PRODUCTS foreign key (suppliers_id) references supplier (id);