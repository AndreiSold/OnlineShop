alter table order_ drop foreign key FK_CUSTOMER_FROM_ORDER, modify customer_id integer;
alter table customer modify id integer auto_increment;
alter table order_ add constraint FK_CUSTOMER_FROM_ORDER foreign key (customer_id) references customer (id);

alter table stock drop foreign key FK_LOCATION_FROM_STOCK, modify location_id integer;
alter table order__shipped_from drop foreign key FK_SHIPPED_FROM_FROM_ORDER_SHIPPED_FROM, modify shipped_from_id integer;
alter table location modify id integer auto_increment;
alter table stock add constraint FK_LOCATION_FROM_STOCK foreign key (location_id) references location (id);
alter table order__shipped_from add constraint FK_SHIPPED_FROM_FROM_ORDER_SHIPPED_FROM foreign key (shipped_from_id) references location (id);

alter table order__shipped_from drop foreign key FK_ORDER_FROM_ORDER_SHIPPED_FROM, modify orders_id integer;
alter table order_detail drop foreign key FK_ORDER_FROM_ORDER_DETAIL, modify order_id integer;
alter table order_ modify id integer auto_increment;
alter table order_detail add constraint FK_ORDER_FROM_ORDER_DETAIL foreign key (order_id) references order_ (id);
alter table order__shipped_from add constraint FK_ORDER_FROM_ORDER_SHIPPED_FROM foreign key (orders_id) references order_ (id);

alter table order_detail modify id integer auto_increment;

alter table order_detail drop foreign key FK_PRODUCT_FROM_ORDER_DETAIL, modify product_id integer;
alter table product_category_products drop foreign key FK_PRODUCT_FROM_PRODUCT_CATEGORY_PRODUCTS, modify products_id integer;
alter table stock drop foreign key FK_PRODUCT_FROM_STOCK, modify product_id integer;
alter table supplier_products drop foreign key FK_PRODUCT_FROM_SUPPLIER_PRODUCTS, modify products_id integer;
alter table product modify id integer auto_increment;
alter table order_detail add constraint FK_PRODUCT_FROM_ORDER_DETAIL foreign key (product_id) references product (id);
alter table product_category_products add constraint FK_PRODUCT_FROM_PRODUCT_CATEGORY_PRODUCTS foreign key (products_id) references product (id);
alter table stock add constraint FK_PRODUCT_FROM_STOCK foreign key (product_id) references product (id);
alter table supplier_products add constraint FK_PRODUCT_FROM_SUPPLIER_PRODUCTS foreign key (products_id) references product (id);

alter table product_category_products drop foreign key FK_PRODUCT_CATEGORY_FROM_PRODUCT_CATEGORY_PRODUCTS, modify product_categories_id integer;
alter table product_category modify id integer auto_increment;
alter table product_category_products add constraint FK_PRODUCT_CATEGORY_FROM_PRODUCT_CATEGORY_PRODUCTS foreign key (product_categories_id) references product_category (id);

alter table stock modify id integer auto_increment;

alter table supplier_products drop foreign key FK_SUPPLIER_FROM_SUPPLIER_PRODUCTS, modify suppliers_id integer;
alter table supplier modify id integer auto_increment;
alter table supplier_products add constraint FK_SUPPLIER_FROM_SUPPLIER_PRODUCTS foreign key (suppliers_id) references supplier (id);