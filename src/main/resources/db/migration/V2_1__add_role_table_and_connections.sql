insert into hibernate_sequence values ( 1001 );

create table role (
  id integer not null,
  name varchar(255),
  primary key (id)
) engine=InnoDB;

create table customer_roles (
  customers_id integer not null,
  roles_id integer not null
) engine=InnoDB;

alter table customer_roles add constraint FK_ROLE_FROM_CUSTOMER_ROLES foreign key (roles_id) references role (id);
alter table customer_roles add constraint FK_CUSTOMER_FROM_CUSTOMER_ROLES foreign key (customers_id) references customer (id);