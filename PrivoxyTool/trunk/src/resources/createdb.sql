drop table if exists properties;
create table properties(
    property varchar(32) not null,
    val varchar(32) not null
);

create unique index uq_properties on properties(property);


drop table if exists privoxylog;
create table privoxylog(
	id identity,
	tstamp timestamp not null,	
    message varchar(2048),
    eventtype int not null,
    fileurl varchar(512),	-- everything up to ? 
    domain varchar(128),
    secondleveldomain varchar(128)    
);

create unique index uq_line on privoxylog(tstamp, message);

insert into properties (property, val) values ('version', '1');