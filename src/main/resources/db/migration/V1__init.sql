create table employee (
    id integer not null auto_increment,
    first_name varchar(150),
    last_name varchar(150),
    primary key (id)
);

insert into employee (first_name, last_name) values ('jon', 'great');
insert into employee (first_name, last_name) values ('david', 'cool');
insert into employee (first_name, last_name) values ('aaron', 'daxter');