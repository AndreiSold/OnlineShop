create table revenue (
  id integer not null,
  location_id integer,
  sum double precision ,
  timestamp date,
  primary key (ID)
) engine=InnoDB;

alter table revenue add constraint FK_LOCATION_FROM_REVENUE foreign key (location_id) references location(id);