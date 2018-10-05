insert into customer (first_name, last_name, password, username, id) values ('admin', 'admin', '$2a$10$DLe04AnWuBhJNjjoO0UUD.B.cfWwxX5RTi39.pg/94WgGLWDliQPy', 'admin', 1001);
insert into customer_roles (customers_id, roles_id) values (1001, 1);
insert into customer_roles (customers_id, roles_id) values (1001, 2);