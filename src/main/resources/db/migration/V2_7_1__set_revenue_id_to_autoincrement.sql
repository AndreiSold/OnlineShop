-- alter table revenue drop foreign key FK_LOCATION_FROM_REVENUE, modify location_id integer;
alter table revenue modify id integer auto_increment;
-- alter table revenue add constraint FK_LOCATION_FROM_REVENUE foreign key (location_id) references location(id);