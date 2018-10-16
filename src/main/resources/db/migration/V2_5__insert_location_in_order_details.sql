alter table order_detail
add column location_id integer;

alter table order_detail add constraint FK_LOCATION_FROM_ORDER_DETAIL foreign key (location_id) references location (id);