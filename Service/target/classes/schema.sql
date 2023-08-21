drop table if exists Users;
create table if not exists Users (
	id identity,
	email VARCHAR(128),
	password VARCHAR(128)
);
