drop table hibernate_sequence;

alter table customer_roles drop foreign key FK_ROLE_FROM_CUSTOMER_ROLES, modify roles_id integer;
alter table customer_roles drop foreign key FK_CUSTOMER_FROM_CUSTOMER_ROLES, modify customers_id integer;
alter table role modify id integer auto_increment;
alter table customer_roles add constraint FK_ROLE_FROM_CUSTOMER_ROLES foreign key (roles_id) references role (id);
alter table customer_roles add constraint FK_CUSTOMER_FROM_CUSTOMER_ROLES foreign key (customers_id) references customer (id);