insert into customer (first_name, last_name, password, username, id) values ('admin', 'admin', '1234', 'admin', 1001);
insert into customer_roles (customers_id, roles_id) values (1001, 1);
insert into customer_roles (customers_id, roles_id) values (1001, 2);

update customer set password = '1234' where username = 'andreiSold';